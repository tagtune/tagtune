package com.ll.tagtune.boundedContext.tagBoard.controller;

import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("usr/category")
@RequiredArgsConstructor
public class TagBoardController {
    private final TagBoardService tagBoardService;

    @GetMapping("/tagBoard")
    public String showTagBoard(Model model, @RequestParam(value = "kw", defaultValue = "") String kw) {
        List<TagBoard> tagBoardList = tagBoardService.findAll();
        List<TagBoard> top3TagBoardList = tagBoardService.findTop3ByPopularity(0L);
        List<TagBoard> filteringTagBoardList = tagBoardService.findByTagBoardNameLike("%" + kw + "%");

        if (!kw.equals("")) {
            model.addAttribute("tagBoardList", filteringTagBoardList);
        } else {
            model.addAttribute("tagBoardList", tagBoardList);
        }
        model.addAttribute("top3TagBoardList", top3TagBoardList);
        model.addAttribute("kw", kw);
        return "usr/category/tagBoard";
    }

    @GetMapping("/tag")
    public String showTag(Model model, @RequestParam(value = "id") Long id) {
        TagBoard tagBoard = tagBoardService.findById(id).get();
        model.addAttribute("tagBoard", tagBoard);

        return "usr/category/tag";
    }
}
