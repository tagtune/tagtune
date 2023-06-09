package com.ll.tagtune.boundedContext.reply.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.service.CommentService;
import com.ll.tagtune.boundedContext.reply.entity.Reply;
import com.ll.tagtune.boundedContext.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReplyController {
    private final Rq rq;
    private final ReplyService replyService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/track/{trackId}/comment/{commentId}")
    public String getReply(
            @PathVariable Long trackId,
            @PathVariable Long commentId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        RsData<List<Reply>> replyRsdata = replyService.getReply(commentService.findById(commentId));
        redirectAttributes.addFlashAttribute("replies", replyRsdata.getData());

        return "redirect:/track/"+ trackId +"/comment";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/track/{trackId}/comment/{parentCommentId}/reply")
    public String saveReply(
            @PathVariable Long trackId,
            @PathVariable Long parentCommentId,
            String iReply
    ) {
        RsData<Reply> replyRsData = replyService.saveReply(rq.getMember(), parentCommentId, iReply);

        return rq.redirectWithMsg("/track/{trackId}/comment", replyRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/track/{trackId}/comment/{replyId}")
    public String deleteReply(
            @PathVariable Long trackId,
            @PathVariable Long replyId
    ) {
        RsData<Void> deleteReplyRsDate = replyService.deleteReply(replyId, rq.getMember().getId());

        return rq.redirectWithMsg("/track/"+ trackId +"/comment",deleteReplyRsDate);
    }
}
