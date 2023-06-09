package com.ll.tagtune.boundedContext.comment.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.service.CommentService;
import com.ll.tagtune.boundedContext.reply.entity.Reply;
import com.ll.tagtune.boundedContext.reply.service.ReplyService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.repository.TrackRepository;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/track/comment")
@RequiredArgsConstructor
public class CommentController {
    private final Rq rq;
    private final CommentService commentService;
    private final TrackService trackService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/track/{trackId}/comment")
    public String showComment(@PathVariable Long trackId, Model model) {

        Track track = trackService.getTrack(trackId);

        List<CommentResponseDTO> commentsDTO = commentService.getCommentsWithReplies(track);
        model.addAttribute("comments", commentsDTO);
        model.addAttribute("track", track);

        // 이전 요청에서 전달받은 모델 데이터를 가져옵니다.
        List<Reply> replies = (List<Reply>) model.getAttribute("replies");
        model.addAttribute("replies", replies);
        return "usr/comment/commentPage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/track/{trackId}/comment")
    public String saveComment(@PathVariable Long trackId, String iComment) {
        if (iComment == null || iComment.length() == 0) {
            return rq.historyBack("댓글을 입력해주세요.");
        }

        RsData<Comment> commentRsData =
                commentService.saveComment(
                        iComment,
                        trackService.getTrack(trackId),
                        rq.getMember()
                );

        if (commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        return rq.redirectWithMsg("/track/"+trackId+"/comment", commentRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/track/{trackId}/comment/{commentId}")
    public String deleteComment(@PathVariable Long trackId, @PathVariable Long commentId) {
        RsData<Void> deleteCmtRsData = commentService.deleteComment(commentId, rq.getMember().getId());

        System.out.println("실행은 되는것인가?");
        return rq.redirectWithMsg("/track/"+trackId+"/comment",deleteCmtRsData);
    }
}
