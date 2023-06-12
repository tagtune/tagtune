package com.ll.tagtune.boundedContext.recommend.service;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.memberFavor.service.FavorService;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;
import com.ll.tagtune.boundedContext.tagVote.service.TagVoteService;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendService {
    private final FavorService favorService;
    private final TagVoteService tagVoteService;

    public List<TrackInfoDTO> getFavoriteList(final Member member) {
        List<Tag> tags = favorService.getFavorTags(member.getId())
                .stream()
                .map(FavorTag::getTag)
                .toList();

        Set<TrackSearchDTO> rawRecommends = new LinkedHashSet<>();
        tags.forEach(tag -> rawRecommends.addAll(SearchEndpoint.getTracksFromTag(tag.getTagName())));

        return SearchEndpoint.getTrackInfos(rawRecommends.stream().toList());
    }

    public List<TrackInfoDTO> getPersonalList(final Member member) {
        final List<String> tags = tagVoteService.getTagVotesCount(member.getId())
                .stream()
                .map(TagVoteCountDTO::getTagName)
                .toList();

        Set<TrackSearchDTO> rawRecommends = new LinkedHashSet<>();
        tags.forEach(tag -> rawRecommends.addAll(SearchEndpoint.getTracksFromTag(tag)));

        return SearchEndpoint.getTrackInfos(rawRecommends.stream().toList());
    }
}
