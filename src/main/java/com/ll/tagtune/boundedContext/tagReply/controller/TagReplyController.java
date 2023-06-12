package com.ll.tagtune.boundedContext.tagReply.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.tagComment.service.TagCommentService;
import com.ll.tagtune.boundedContext.tagReply.entity.TagReply;
import com.ll.tagtune.boundedContext.tagReply.service.TagReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class TagReplyController {
    private final Rq rq;
    private final TagReplyService tagReplyService;
    private final TagCommentService tagCommentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/tag/{parentCommentId}/{iReplyId}")
    public String saveReply(
            @PathVariable Long parentCommentId,
            @PathVariable Long iReplyId,
            Model model,
            String iReply
    ) {
        model.addAttribute("iReplyId", iReplyId);
        Long tagBoardId = tagCommentService.findById(parentCommentId).getTagBoard().getId();
        RsData<TagReply> replyRsData = tagReplyService.saveReply(rq.getMember(), parentCommentId, iReply);

        return rq.redirectWithMsg("/category/tag?id=" + tagBoardId, replyRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/tag/{parentCommentId}/{iReplyId}")
    public String deleteReply(
            @PathVariable Long parentCommentId,
            @PathVariable Long iReplyId
    ) {
        Long tagBoardId = tagCommentService.findById(parentCommentId).getTagBoard().getId();
        RsData<Void> deleteReplyRsDate = tagReplyService.deleteReply(iReplyId, rq.getMember().getId());

        return rq.redirectWithMsg("/category/tag?id=" + tagBoardId, deleteReplyRsDate);
    }
}
