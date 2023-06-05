package com.ll.tagtune.boundedContext.tagComment.controller;

import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import com.ll.tagtune.boundedContext.tagComment.service.TagCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("usr/category/tag")
@RequiredArgsConstructor
public class TagCommentController {
    private final TagCommentService tagCommentService;
    private final TagBoardService tagBoardService;

    @PostMapping("/create/{tagBoardName}")
    public String createComment(Model model, @PathVariable("tagBoardName") String tagBoardName, @RequestParam String content) {
        TagBoard tagBoard = this.tagBoardService.findByTagBoardName(tagBoardName).get();
        return String.format("redirect:/usr/category/tag/%s", tagBoardName);
    }
}
