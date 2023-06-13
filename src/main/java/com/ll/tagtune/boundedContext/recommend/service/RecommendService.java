package com.ll.tagtune.boundedContext.recommend.service;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.memberFavor.service.FavorService;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;
import com.ll.tagtune.boundedContext.tagVote.service.TagVoteService;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendService {
    private final TrackService trackService;
    private final FavorService favorService;
    private final TagVoteService tagVoteService;

    private List<TrackInfoDTO> getTrackInfos(final List<String> tagNames) {
        List<TrackInfoDTO> result = new CopyOnWriteArrayList<>();
        List<TrackSearchDTO> emptyTracks = new ArrayList<>();

        tagNames.parallelStream()
                .flatMap(tag -> SearchEndpoint.getTracksFromTag(tag).stream().distinct())
                .forEach(trackSearchDTO -> processTrackSearchDTO(trackSearchDTO, result, emptyTracks));

        result.addAll(SearchEndpoint.getTrackInfos(emptyTracks));
        return result;
    }

    public List<TrackInfoDTO> getFavoriteList(final Member member) {
        final List<FavorTag> tags = favorService.getFavorTags(member.getId());
        List<TrackInfoDTO> result = getTrackInfos(tags.stream()
                .map(FavorTag::getTag)
                .map(Tag::getTagName)
                .toList()
        );

        Map<TrackInfoDTO, Long> trackScores = new HashMap<>();
        for (TrackInfoDTO track : result) {
            long score = 0;
            for (FavorTag tag : tags)
                if (track.getTags().contains(tag.getTag().getTagName()))
                    score++;

            trackScores.put(track, score);
        }

        return trackScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(AppConfig.getRecommendSize())
                .toList();
    }

    public List<TrackInfoDTO> getPersonalList(final Member member) {
        final List<TagVoteCountDTO> tags = tagVoteService.getTagVotesCount(member.getId());
        List<TrackInfoDTO> result = getTrackInfos(tags.stream()
                .map(TagVoteCountDTO::getTagName)
                .toList()
        );

        Map<TrackInfoDTO, Long> trackScores = new HashMap<>();
        for (TrackInfoDTO track : result) {
            long score = 0;
            for (TagVoteCountDTO tag : tags) {
                if (track.getTags().contains(tag.getTagName()))
                    score += tag.getVoteCount();
            }
            trackScores.put(track, score);
        }

        return trackScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(AppConfig.getRecommendSize())
                .toList();
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
}