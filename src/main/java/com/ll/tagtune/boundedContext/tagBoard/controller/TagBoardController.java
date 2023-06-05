package com.ll.tagtune.boundedContext.tagBoard.controller;

import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("usr/category")
@RequiredArgsConstructor
public class TagBoardController {
    private TagBoardService tagBoardService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tagBoard")
    public String showTagBoard() {
        return "usr/category/tagBoard";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tag")
    public String showTag() {
        return "usr/category/tag";
    }

}
