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
@RequestMapping("category")
@RequiredArgsConstructor
public class TagBoardController {
    private final TagBoardService tagBoardService;
    /**
     * 검색 필터링에 사용할 키워드 "kw"
     * "kw"가 없을 경우 필터링 하지 않은 모든 tagBoardList 반환
     * "kw"가 있을 경우 "kw"가 포함된 모든 tagBoardName 반환 -> "%" + kw + "%"
     */
    @GetMapping("/tagBoard")
    public String showTagBoard(Model model, @RequestParam(value = "kw", defaultValue = "") String kw) {
        List<TagBoard> top3TagBoardList = tagBoardService.findTop3ByOrderByPopularityDesc();

        if (!kw.equals("")) {
            List<TagBoard> filteringTagBoardList = tagBoardService.findByTagBoardNameLike("%" + kw + "%");
            model.addAttribute("tagBoardList", filteringTagBoardList);
        } else {
            List<TagBoard> tagBoardList = tagBoardService.findAll();
            model.addAttribute("tagBoardList", tagBoardList);
        }
        model.addAttribute("top3TagBoardList", top3TagBoardList);
        model.addAttribute("kw", kw);
        return "usr/category/tagBoard";
    }
}
