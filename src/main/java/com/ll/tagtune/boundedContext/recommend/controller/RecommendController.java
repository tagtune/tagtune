package com.ll.tagtune.boundedContext.recommend.controller;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.boundedContext.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final Rq rq;

    @GetMapping("/list")
    public String list() {
        return "usr/recommend/list";
    }

    @GetMapping("/trending")
    public String trending(Model model) {
        model.addAttribute("tracks", SearchEndpoint.getTrendingList());

        return "usr/recommend/trending";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favorite")
    public String favorite(Model model) {
        model.addAttribute("tracks", recommendService.getFavoriteList(rq.getMember()));

        return "usr/recommend/favorite";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/personal")
    public String personal(Model model) {
        model.addAttribute("tracks", recommendService.getPersonalList(rq.getMember()));

        return "usr/recommend/personal";
    }
}
