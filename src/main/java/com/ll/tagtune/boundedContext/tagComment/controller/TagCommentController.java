package com.ll.tagtune.boundedContext.tagComment.controller;

import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import com.ll.tagtune.boundedContext.tagComment.service.TagCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("usr/category/tag")
@RequiredArgsConstructor
public class TagCommentController {
    private final TagCommentService tagCommentService;
    private final TagBoardService tagBoardService;
}
