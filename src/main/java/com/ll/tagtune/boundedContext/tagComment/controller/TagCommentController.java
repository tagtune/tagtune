package com.ll.tagtune.boundedContext.tagComment.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import com.ll.tagtune.boundedContext.tagComment.dto.TagCommentResponseDTO;
import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import com.ll.tagtune.boundedContext.tagComment.service.TagCommentService;
import com.ll.tagtune.boundedContext.tagReply.dto.TagReplyResponseDTO;
import com.ll.tagtune.boundedContext.tagReply.service.TagReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class TagCommentController {
    private final Rq rq;
    private final TagCommentService tagCommentService;
    private final TagBoardService tagBoardService;
    private final TagReplyService tagReplyService;

    @GetMapping("/tag")
    public String showPage(Model model, @RequestParam(value = "id", defaultValue = "") Long id) {
        if (id == null) {
            return rq.historyBack("잘못된 접근입니다.");
        }
        TagBoard tagBoard = tagBoardService.findById(id).get();
        model.addAttribute("tagBoard", tagBoard);

        tagBoardService.updatePopularity(tagBoard);

        List<TagCommentResponseDTO> commentsDTO = tagCommentService.getCommentsWithReplies(tagBoard);
        List<TagReplyResponseDTO> replyResponseDTO = tagReplyService.getReplies(tagBoard);

        model.addAttribute("comments", commentsDTO);
        model.addAttribute("tagId", id);

        // 이전 요청에서 전달받은 모델 데이터를 가져옵니다.
        model.addAttribute("replies", replyResponseDTO);

        return "usr/category/tag";
    }

    @GetMapping("/tagId/{tagId}")
    public String getTagBoardPage(@PathVariable Long tagId) {
        Optional<TagBoard> oTagBoard = tagBoardService.findByTagId(tagId);

        return oTagBoard.map(tagBoard -> "redirect:/category/tag?id=" + tagBoard.getId())
                .orElseGet(() -> rq.historyBack("잘못된 접근입니다."));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/tag")
    public String saveComment(@RequestParam(value = "id", defaultValue = "") Long tagBoardId, String iComment) {
        if (iComment == null || iComment.length() == 0) {
            return rq.historyBack("댓글을 입력해주세요.");
        }

        RsData<TagComment> commentRsData =
                tagCommentService.saveComment(
                        iComment,
                        tagBoardService.findById(tagBoardId).get(),
                        rq.getMember()
                );

        if (commentRsData.isFail()) {
            return rq.historyBack(commentRsData);
        }

        return rq.redirectWithMsg("/category/tag?id=" + tagBoardId, commentRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/tag/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        Long tagBoardId = tagCommentService.findById(commentId).getTagBoard().getId();
        RsData<Void> deleteCmtRsData = tagCommentService.deleteComment(commentId, rq.getMember().getId());

        return rq.redirectWithMsg("/category/tag?id=" + tagBoardId, deleteCmtRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/tag/{id}")
    public String modifyComment(@PathVariable Long id, String modifyContent) {
        Long tagBoardId = tagCommentService.findById(id).getTagBoard().getId();
        RsData<TagComment> modifyCommentRsData = tagCommentService.modifyComment(id, modifyContent, rq.getMember());

        return rq.redirectWithMsg("/category/tag?id=" + tagBoardId, modifyCommentRsData);
    }
}
