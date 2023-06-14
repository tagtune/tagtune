package com.ll.tagtune.boundedContext.lyric.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Language;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.service.LyricService;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LyricController {
    private final LyricService lyricService;
    private final TrackService trackService;
    private final Rq rq;

    @GetMapping("/track/{trackId}/lyric")
    public String showLyric(@PathVariable Long trackId, Model model) {
        Optional<Lyric> lyric_ko = lyricService.showLyric(trackId, Language.KOREAN);
        if (lyric_ko.isEmpty()) { //한글가사 있는지 없는지
            RsData<Lyric> lyricRsData = lyricService.saveLyric
                    (
                            trackService.getTrack(trackId).getData(),
                            Language.KOREAN
                    );
            model.addAttribute("lyric_ko", lyricRsData.getData());
        } else {
            model.addAttribute("lyric_ko", lyric_ko.get());
        }

        Optional<Lyric> lyric_en = lyricService.showLyric(trackId, Language.ENGLISH);
        if (lyric_ko.isEmpty()) { //영어가사 있는지 없는지
            RsData<Lyric> lyricRsData = lyricService.saveLyric
                    (
                            trackService.getTrack(trackId).getData(),
                            Language.ENGLISH
                    );
            model.addAttribute("lyric_en", lyricRsData.getData());
        } else {
            model.addAttribute("lyric_en", lyric_en.get());
        }
        return "usr/lyric/lyric";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/track/{trackId}/modifyLyric")
    public String modifyLyric(@PathVariable Long trackId,
                              @RequestParam(value = "lang", defaultValue = "") String lang,
                              Model model) {
        model.addAttribute("lang", lang);

        Optional<Lyric> lyric;
        if (lang.equals("kor")) {
            lyric = lyricService.showLyric(trackId, Language.KOREAN);
            model.addAttribute("lyric", lyric.get());
        } else {
            lyric = lyricService.showLyric(trackId, Language.ENGLISH);
            model.addAttribute("lyric", lyric.get());
        }

        return "usr/lyric/modifyLyric";
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/track/{trackId}/modifyLyric/{lang}")
    public String modifyLyric(@PathVariable Long trackId,
                              @PathVariable String lang,
                              String content) {
        RsData<Lyric> lyricRsData;
        if (lang.equals("kor")) {
            lyricRsData = lyricService.modifyLyric(trackId, content, Language.KOREAN);
        } else {
            lyricRsData = lyricService.modifyLyric(trackId, content, Language.ENGLISH);
        }

        return rq.redirectWithMsg("/track/"+trackId+"/lyric", lyricRsData.getMsg());
    }
}
