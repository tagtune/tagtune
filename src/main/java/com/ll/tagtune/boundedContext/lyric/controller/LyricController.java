package com.ll.tagtune.boundedContext.lyric.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Language;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.service.LyricService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LyricController {
    private final LyricService lyricService;
    private final TrackService trackService;
    private final Rq rq;

    @GetMapping("/track/{trackId}/lyric")
    public String showLyric(@PathVariable Long trackId, Model model) {
        model.addAttribute("lyric_ko", lyricService.showLyricDTO(trackId, Language.KOREAN));
        model.addAttribute("lyric_en", lyricService.showLyricDTO(trackId, Language.ENGLISH));

        return "usr/lyric/lyric";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/track/{trackId}/modifyLyric")
    public String modifyLyric(
            @PathVariable Long trackId,
            @RequestParam(value = "lang", defaultValue = "") String lang,
            Model model
    ) {
        model.addAttribute("lyric", lyricService.showLyricDTO(trackId, Language.nameOf(lang)));

        return "usr/lyric/modifyLyric";
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/track/{trackId}/modifyLyric/{lang}")
    public String modifyLyric(
            @PathVariable Long trackId,
            @PathVariable String lang,
            String content
    ) {
        RsData<Track> rsTrack = trackService.getTrack(trackId);
        if (rsTrack.isFail()) return rq.historyBack(rsTrack);

        RsData<Lyric> lyricRsData = lyricService.writeLyric(rsTrack.getData(), content, Language.nameOf(lang));

        return rq.redirectWithMsg("/track/" + trackId + "/lyric", lyricRsData.getMsg());
    }
}
