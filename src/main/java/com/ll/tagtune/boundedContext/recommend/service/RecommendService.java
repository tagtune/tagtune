package com.ll.tagtune.boundedContext.recommend.service;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.memberFavor.service.FavorService;
import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;
import com.ll.tagtune.boundedContext.tagVote.service.TagVoteService;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendService {
    private final TrackService trackService;
    private final FavorService favorService;
    private final TagVoteService tagVoteService;

    public List<TrackInfoDTO> getFavoriteList(final Member member) {
        final List<FavorTag> tags = favorService.getFavorTags(member.getId());
        Set<TrackSearchDTO> rawRecommends = new HashSet<>();
        List<TrackInfoDTO> result = new CopyOnWriteArrayList<>();
        List<TrackSearchDTO> emptyTracks = new ArrayList<>();

        tags.parallelStream()
                .map(FavorTag::getTag)
                .flatMap(tag -> SearchEndpoint.getTracksFromTag(tag.getTagName()).stream().distinct())
                .forEach(rawRecommends::add);

        rawRecommends.parallelStream()
                .forEach(trackSearchDTO -> processTrackSearchDTO(trackSearchDTO, result, emptyTracks));

        result.addAll(SearchEndpoint.getTrackInfos(emptyTracks));

        return result;
    }

    public List<TrackInfoDTO> getPersonalList(final Member member) {
        final List<TagVoteCountDTO> tags = tagVoteService.getTagVotesCount(member.getId());

        Set<TrackSearchDTO> rawRecommends = new HashSet<>();
        List<TrackInfoDTO> result = new CopyOnWriteArrayList<>();
        List<TrackSearchDTO> emptyTracks = new ArrayList<>();

        tags.parallelStream()
                .flatMap(tag -> SearchEndpoint.getTracksFromTag(tag.getTagName()).stream().distinct())
                .forEach(rawRecommends::add);

        rawRecommends.parallelStream()
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
}