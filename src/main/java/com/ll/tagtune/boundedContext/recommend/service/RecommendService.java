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

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {
    private final TrackService trackService;
    private final FavorService favorService;
    private final TagVoteService tagVoteService;
    private final TrendingRepository trendingRepository;
    private final TrackInfoSnapshotRepository trackInfoSnapshotRepository;

    private List<TrackInfoDTO> getTrackInfos(final List<String> tagNames) {
        List<TrackInfoDTO> result = new CopyOnWriteArrayList<>();
        List<TrackSearchDTO> emptyTracks = new ArrayList<>();

        tagNames.parallelStream()
                .flatMap(tag -> SearchEndpoint.getTracksFromTag(tag).stream().distinct())
                .forEach(trackSearchDTO -> processTrackSearchDTO(trackSearchDTO, result, emptyTracks));

        result.addAll(SearchEndpoint.getTrackInfos(emptyTracks));
        return result;
    }

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

    private List<TrackInfoDTO> setFavoriteList(final Member member, TrackInfoSnapshot snapshot) {
        final List<FavorTag> tags = favorService.getFavorTags(member.getId());
        List<TrackInfoDTO> rawResult = getTrackInfos(tags.stream()
                .map(FavorTag::getTag)
                .map(Tag::getTagName)
                .toList()
        );

        Map<TrackInfoDTO, Long> trackScores = new HashMap<>();
        for (TrackInfoDTO track : rawResult) {
            long score = 0;
            for (FavorTag tag : tags)
                if (track.getTags().contains(tag.getTag().getTagName()))
                    score++;

            trackScores.put(track, score);
        }

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
        TrackInfoSnapshot result =
                trackInfoSnapshotRepository.findByMember_IdAndRecommendType(member.getId(), RecommendType.FAVORITE)
                        .orElseGet(() -> TrackInfoSnapshot.builder()
                                .recommendType(RecommendType.FAVORITE)
                                .member(member)
                                .build()
                        );

        if (result.isExpired()) return setFavoriteList(member, result);

        return TrackInfosUt.deserialize(result.getTrackInfoListJson());
    }

    private List<TrackInfoDTO> setPersonalList(final Member member, TrackInfoSnapshot snapshot) {
        final List<TagVoteCountDTO> tags = tagVoteService.getTagVotesCount(member.getId());
        List<TrackInfoDTO> rawResult = getTrackInfos(tags.stream()
                .map(TagVoteCountDTO::getTagName)
                .toList()
        );

        Map<TrackInfoDTO, Long> trackScores = new HashMap<>();
        for (TrackInfoDTO track : rawResult) {
            long score = 0;
            for (TagVoteCountDTO tag : tags) {
                if (track.getTags().contains(tag.getTagName()))
                    score += tag.getVoteCount();
            }
            trackScores.put(track, score);
        }

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
        TrackInfoSnapshot result =
                trackInfoSnapshotRepository.findByMember_IdAndRecommendType(member.getId(), RecommendType.PERSONAL)
                        .orElseGet(() -> TrackInfoSnapshot.builder()
                                .recommendType(RecommendType.PERSONAL)
                                .member(member)
                                .build()
                        );

        if (result.isExpired()) return setPersonalList(member, result);

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
        // debug
        // System.out.println("[D2BUG]: Trending Update");
        List<TrendingTrack> trendingTracks = TrendingTrack.of(SearchEndpoint.getTrendingList());
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