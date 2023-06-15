package com.ll.tagtune.boundedContext.comment.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.service.CommentService;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final Rq rq;
    private final CommentService commentService;
    private final TrackService trackService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/track/{trackId}/comment")
    public String saveComment(@PathVariable Long trackId, String iComment) {
        if (iComment == null || iComment.length() == 0) {
            return rq.historyBack("댓글을 입력해주세요.");
        }

        RsData<Comment> commentRsData =
                commentService.saveComment(
                        iComment,
                        trackService.getTrack(trackId).getData(),
                        rq.getMember()
                );

        if (commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        return rq.redirectWithMsg("/track/" + trackId, commentRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/track/{trackId}/comment/{commentId}")
    public String deleteComment(@PathVariable Long trackId, @PathVariable Long commentId) {
        RsData<Void> deleteCmtRsData = commentService.deleteComment(commentId, rq.getMember().getId());

        return rq.redirectWithMsg("/track/" + trackId, deleteCmtRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/track/{trackId}/comment/{commentId}/modify")
    public String modifyComment(
            @PathVariable Long trackId,
            @PathVariable Long commentId,
            String modifyComment
    ) {
        RsData<Comment> modifyCommentRsData =
                commentService.modifyComment(commentId, modifyComment, rq.getMember());

        return rq.redirectWithMsg("/track/" + trackId, modifyCommentRsData);
    }
}
