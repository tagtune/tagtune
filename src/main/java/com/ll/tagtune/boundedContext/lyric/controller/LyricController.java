package com.ll.tagtune.boundedContext.lyric.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lyric")
public class LyricController {
    @GetMapping("/")
    public String lyric(HttpSession session) {
        return "/lyric/lyric";
    }

    @GetMapping("/modify")
    public String lyricModify(HttpSession session) {
        return "/lyric/modifyLyric";
    }
}
