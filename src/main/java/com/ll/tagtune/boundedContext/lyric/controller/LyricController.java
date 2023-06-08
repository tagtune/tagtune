package com.ll.tagtune.boundedContext.lyric.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lyric")
public class LyricController {
    @GetMapping("/")
    public String lyric(HttpSession session) {
        return "/usr/lyric/lyricPage.html";
    }

    @GetMapping("/modify")
    public String lyricModify(HttpSession session) {
        return "/usr/lyric/modifyLyric.html";
    }
}
