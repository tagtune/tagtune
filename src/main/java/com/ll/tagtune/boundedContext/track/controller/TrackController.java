package com.ll.tagtune.boundedContext.track.controller;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.ll.tagtune.boundedContext.comment.service.CommentService;
import com.ll.tagtune.boundedContext.reply.dto.ReplyDTO;
import com.ll.tagtune.boundedContext.reply.service.ReplyService;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import com.ll.tagtune.boundedContext.track.service.TrackTagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final TagService tagService;
    private final TrackTagService trackTagService;
    private final CommentService commentService;
    private final ReplyService replyService;
    private final Rq rq;

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "trackName", defaultValue = "") String trackName,
            @RequestParam(value = "artistName", defaultValue = "") String artistName,
            Model model
    ) {
        if (!trackName.isBlank()) {
            if (artistName.isBlank()) rq.historyBack("트랙 제목을 입력해야합니다.");
            List<TrackSearchDTO> rawTracks = (artistName.isBlank() ?
                    SearchEndpoint.searchTrack(trackName) :
                    SearchEndpoint.searchTrack(trackName, artistName))
                    .getTracks();
            model.addAttribute("tracks", rawTracks);
        }

        return "usr/track/searchForm";
    }

    @PostMapping("/search")
    public String searchResult(TrackSearchDTO searchDTO) {
        if (searchDTO.name.isBlank() || searchDTO.artist.isBlank()) return rq.historyBack("잘못된 접근입니다.");
        final Long trackId = trackService.getTrackByTitleAndArtist(searchDTO.name, searchDTO.artist)
                .map(Track::getId)
                .orElseGet(() -> trackService.setTrackInfo(searchDTO).getId());

        return "redirect:/track/" + trackId;
    }

    @GetMapping("/{trackId}")
    public String detail(@PathVariable Long trackId, Model model) {
        RsData<TrackDetailDTO> rsTrack = rq.isLogin() ?
                trackService.getTrackDetailWithVote(trackId, rq.getMember().getId()) :
                trackService.getTrackDetail(trackId);
        if (rsTrack.isFail()) return rq.historyBack(rsTrack);

        List<CommentResponseDTO> commentsWithReplies =
                commentService.getCommentsWithReplies(rsTrack.getData());

        List<ReplyDTO> replies = commentsWithReplies
                .stream()
                .flatMap(comment -> replyService.getReplies(comment.getCommentId()).getData().stream())
                .collect(Collectors.toList());

        model.addAttribute("trackDetail", rsTrack.getData());
        model.addAttribute("comments", commentsWithReplies);
        model.addAttribute("replies", replies);

        return "usr/track/detail";
    }

    private record TagForm(@NotBlank String tagName) {
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{trackId}/tag")
    public String addTag(@PathVariable Long trackId, @Valid TagForm tagForm) {
        RsData<Track> rsTrack = trackService.getTrack(trackId);
        if (rsTrack.isFail()) return rq.historyBack(rsTrack);
        trackTagService.connect(rsTrack.getData(), tagService.getOrCreateTag(tagForm.tagName));

        return "usr/track/detail";
    }
}