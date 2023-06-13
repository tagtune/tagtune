package com.ll.tagtune.boundedContext.lyric.controller;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Language;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.service.LyricService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LyricController {
    private final LyricService lyricService;
    private final TrackService trackService;

    @GetMapping("/track/{trackId}/lyric")
    public String showLyric(@PathVariable Long trackId, Model model) {
        return "usr/lyric/lyric";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/track/{trackId}/modifyLyric")
    public String modifyLyric(@PathVariable Long trackId, Model model) {
        return "usr/lyric/modifyLyric";
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/track/{trackId}/modifyLyric")
    public String modifyLyric(@PathVariable Long trackId, String content, Language language) {
        lyricService.modifyLyric(trackId, content, language);

        return "/track" + trackId;
    }
}
