package com.ll.tagtune.boundedContext.recommend.service;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.memberFavor.service.FavorService;
import com.ll.tagtune.boundedContext.recommend.entity.RecommendType;
import com.ll.tagtune.boundedContext.recommend.entity.TrackInfoSnapshot;
import com.ll.tagtune.boundedContext.recommend.entity.TrendingTrack;
import com.ll.tagtune.boundedContext.recommend.repository.TrackInfoSnapshotRepository;
import com.ll.tagtune.boundedContext.recommend.repository.TrendingRepository;
import com.ll.tagtune.boundedContext.recommend.util.TrackInfosUt;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;
import com.ll.tagtune.boundedContext.tagVote.service.TagVoteService;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {
    private final TrackService trackService;
    private final FavorService favorService;
    private final TagVoteService tagVoteService;
    private final TrendingRepository trendingRepository;
    private final TrackInfoSnapshotRepository trackInfoSnapshotRepository;

    /**
     * Tag 의 이름으로 Lastfm tag.toptrack 를 병렬로 호출 합니다.
     * 검색 결과에 Track 에 있는지 조회하고, 없다면 Lastfm track.search 를 병렬로 호출합니다.
     *
     * @param tagNames
     * @return List<TrackInfoDTO> Track 과 Tag 정보를 담고 있습니다.
     */
    private List<TrackInfoDTO> getTrackInfos(final List<String> tagNames) {
        List<TrackInfoDTO> result = new CopyOnWriteArrayList<>();
        List<TrackSearchDTO> emptyTracks = new ArrayList<>();

        tagNames.parallelStream()
                .flatMap(tag -> SearchEndpoint.getTracksFromTag(tag).getTracks().stream())
                .forEach(trackSearchDTO -> processTrackSearchDTO(trackSearchDTO, result, emptyTracks));

        result.addAll(SearchEndpoint.getTrackInfos(emptyTracks));
        return result;
    }

    /**
     * Track 엔티티에 해당 Track 의 상세정보가 있는지 name 과 artist 로 조회하고 분류합니다.
     *
     * @param trackSearchDTO Track 테이블에 존재하는지 조회할 데이터입니다.
     * @param result         존재 한다면 result 에 추가합니다.
     * @param emptyTracks    없다면 emptyTrack 에 추가합니다. Lastfm track.search 를 호출해야 하는 목록입니다.
     */
    private void processTrackSearchDTO(
            TrackSearchDTO trackSearchDTO,
            List<TrackInfoDTO> result,
            List<TrackSearchDTO> emptyTracks
    ) {
        trackService.getTrackInfo(trackSearchDTO.getName(), trackSearchDTO.getArtist())
                .ifPresentOrElse(
                        result::add,
                        () -> emptyTracks.add(trackSearchDTO)
                );
    }

    /**
     * Member 의 FavorTag 기반 추천을 생성합니다.
     * FavorTag 중 가장 최근 3개를 통해 검색합니다.
     * 검색 한 결과에 대해 해당 Track 의 Tag 가 FavorTag 와 일치하는지 확인합니다.
     * 일치하는 Tag 하나당 1점을 부여하고, 이 점수를 통해 정렬하여 리턴합니다.
     * 검색 결과는 snapshot 을 통해 저장됩니다.
     * 1 시간에 한번 추천받을 수 있습니다.
     *
     * @param tags     FavorTag 중 최근 추가한 3개
     * @param snapshot
     * @return FavorTag 기반 추천 결과
     */
    private List<TrackInfoDTO> setFavoriteList(final List<FavorTag> tags, TrackInfoSnapshot snapshot) {
        List<TrackInfoDTO> rawResult = getTrackInfos(tags.stream()
                .map(FavorTag::getTag)
                .map(Tag::getTagName)
                .toList()
        );

        Map<TrackInfoDTO, Long> trackScores = rawResult.stream()
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        track -> tags.stream()
                                .filter(tag -> track.getTags().contains(tag.getTag().getTagName()))
                                .count()
                ));

        List<TrackInfoDTO> result = trackScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(AppConfig.getRecommendSize())
                .toList();

        trackInfoSnapshotRepository.save(snapshot.toBuilder()
                .trackInfoListJson(TrackInfosUt.serialize(result))
                .build()
        );

        // debug
        // System.out.println("[D2BUG]: setFavoriteList Update");

        return result;
    }

    /**
     * 선호 태그 기반 추천을 받습니다.
     *
     * @param member
     * @return List<TrackInfoDTO>
     */
    public List<TrackInfoDTO> getFavoriteList(final Member member) {
        final List<FavorTag> tags = favorService.getFavorTagsTop3(member.getId());
        if (tags.isEmpty()) return Collections.emptyList();

        TrackInfoSnapshot result =
                trackInfoSnapshotRepository.findByMember_IdAndRecommendType(member.getId(), RecommendType.FAVORITE)
                        .orElseGet(() -> TrackInfoSnapshot.builder()
                                .recommendType(RecommendType.FAVORITE)
                                .member(member)
                                .build()
                        );

        if (result.isExpired()) return setFavoriteList(tags, result);

        return TrackInfosUt.deserialize(result.getTrackInfoListJson());
    }

    /**
     * Member 의 TagVote 기반 추천을 생성합니다.
     * TagVote 중 Positive 투표가 가장 많은 태그 3개를 통해 검색합니다.
     * 검색 한 결과에 대해 해당 Track 의 Tag 가 TagVote 의 Tag 와 일치하는지 확인합니다.
     * 일치하는 Tag 에 Positive 개수만큼 점수를 부여하고, 이 점수를 통해 정렬하여 리턴합니다.
     * 검색 결과는 snapshot 을 통해 저장됩니다.
     * 1 시간에 한번 추천받을 수 있습니다.
     *
     * @param tags     TagVote 를 TagName 으로 Grouping By 한 결과
     * @param snapshot
     * @return TagVote 기반 추천 결과
     */
    private List<TrackInfoDTO> setPersonalList(final List<TagVoteCountDTO> tags, TrackInfoSnapshot snapshot) {
        List<TrackInfoDTO> rawResult = getTrackInfos(tags.stream()
                .map(TagVoteCountDTO::getTagName)
                .toList()
        );

        Map<TrackInfoDTO, Long> trackScores = rawResult.stream()
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        track -> tags.stream()
                                .filter(tag -> track.getTags().contains(tag.getTagName()))
                                .mapToLong(TagVoteCountDTO::getVoteCount)
                                .sum()
                ));

        List<TrackInfoDTO> result = trackScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(AppConfig.getRecommendSize())
                .toList();

        trackInfoSnapshotRepository.save(snapshot.toBuilder()
                .trackInfoListJson(TrackInfosUt.serialize(result))
                .build()
        );

        // debug
        // System.out.println("[D2BUG]: setPersonalList Update");

        return result;
    }

    /**
     * 조회 태그 기반 추천을 받습니다.
     *
     * @param member
     * @return List<TrackInfoDTO>
     */
    public List<TrackInfoDTO> getPersonalList(final Member member) {
        final List<TagVoteCountDTO> tags = tagVoteService.getTagVotesCount(member.getId());
        if (tags.isEmpty()) return Collections.emptyList();

        TrackInfoSnapshot result =
                trackInfoSnapshotRepository.findByMember_IdAndRecommendType(member.getId(), RecommendType.PERSONAL)
                        .orElseGet(() -> TrackInfoSnapshot.builder()
                                .recommendType(RecommendType.PERSONAL)
                                .member(member)
                                .build()
                        );

        if (result.isExpired()) return setPersonalList(tags, result);

        return TrackInfosUt.deserialize(result.getTrackInfoListJson());
    }

    /**
     * 인기 음악을 불러옵니다.
     *
     * @return TrendingTracks
     */
    public List<TrendingTrack> getTrending() {
        List<TrendingTrack> result = trendingRepository.findAll();

        return result.isEmpty() ? getTrendingSearchData() : result;
    }

    /**
     * lastfm api 의 인기 음악을 갱신합니다.
     */
    private List<TrendingTrack> getTrendingSearchData() {
        List<TrendingTrack> trendingTracks = TrendingTrack.of(SearchEndpoint.getTrendingList().getTracks());
        trendingRepository.saveAll(trendingTracks);

        return trendingTracks;
    }

    /**
     * 1시간에 한번 lastfm api 의 인기 음악을 갱신합니다.
     */
    @Scheduled(fixedRate = 3600000) // 1시간에 한 번씩 실행
    private void scheduledTrending() {
        getTrendingSearchData();
    }
}