package com.ll.tagtune.boundedContext.track.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.tagVote.entity.TagVote;
import com.ll.tagtune.boundedContext.tagVote.service.TagVoteService;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import com.ll.tagtune.boundedContext.track.service.TrackTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trackTag/vote")
@RequiredArgsConstructor
public class TrackTagController {
    private final TrackTagService trackTagService;
    private final TagVoteService tagVoteService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/up/{trackTagId}")
    public String voteUp(@PathVariable Long trackTagId) {
        RsData<TrackTag> rsTrackTag = trackTagService.getTrackTag(trackTagId);
        if (rsTrackTag.isFail()) return rq.historyBack(rsTrackTag);
        Long trackId = rsTrackTag.getData().getTrack().getId();
        RsData<TagVote> tagVote = tagVoteService.vote(Boolean.TRUE, rq.getMember(), rsTrackTag.getData());

        return rq.redirectWithMsg("/track/" + trackId, tagVote);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/down/{trackTagId}")
    public String voteDown(@PathVariable Long trackTagId) {
        RsData<TrackTag> rsTrackTag = trackTagService.getTrackTag(trackTagId);
        if (rsTrackTag.isFail()) return rq.historyBack(rsTrackTag);
        Long trackId = rsTrackTag.getData().getTrack().getId();
        RsData<TagVote> tagVote = tagVoteService.vote(Boolean.FALSE, rq.getMember(), rsTrackTag.getData());

        return rq.redirectWithMsg("/track/" + trackId, tagVote);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel/{trackTagId}")
    public String remove(@PathVariable Long trackTagId) {
        RsData<TrackTag> rsTrackTag = trackTagService.getTrackTag(trackTagId);
        if (rsTrackTag.isFail()) return rq.historyBack(rsTrackTag);
        Long trackId = rsTrackTag.getData().getTrack().getId();
        RsData<Void> result = tagVoteService.cancel(rq.getMember().getId(), trackTagId);
        if (result.isFail()) return rq.historyBack(result);

        return rq.redirectWithMsg("/track/" + trackId, result);
    }
}