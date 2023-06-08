package com.ll.tagtune.boundedContext.track.controller;

import com.ll.tagtune.base.lastfm.ResultParser;
import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackSearchDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import com.ll.tagtune.boundedContext.track.service.TrackTagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final TagService tagService;
    private final TrackTagService trackTagService;
    private final Rq rq;

    private record TrackSearchForm(@NotBlank String title, String artistName) {
    }

    @GetMapping("/search")
    public String search(@Valid TrackSearchForm trackSearchForm, Model model) {
        List<TrackSearchDTO> rawTracks = (trackSearchForm.artistName == null) ?
                ResultParser.searchTracks(trackSearchForm.title) :
                ResultParser.searchTracks(trackSearchForm.title, trackSearchForm.artistName);
        model.addAttribute("tracks", rawTracks);

        return "usr/track/search";
    }

    @GetMapping("/{trackId}")
    public String detail(@PathVariable Long trackId, Model model) {
        RsData<TrackDetailDTO> rsTrack = trackService.getTrackDetail(trackId);
        if (rsTrack.isFail()) return rq.historyBack(rsTrack);
        model.addAttribute("trackDetail", rsTrack.getData());

        return "usr/track/detail";
    }

    private record TagForm(@NotBlank String tagName) {
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{trackId}/tag")
    public String addTag(@PathVariable Long trackId, @Valid TagForm tagForm) {
        RsData<Track> rsTrack = trackService.getTrack(trackId);
        if (rsTrack.isFail()) return rq.historyBack(rsTrack);
        trackTagService.connect(rsTrack.getData(), tagService.getOrCreateTag(tagForm.tagName));

        return "usr/track/detail";
    }
}
