package com.ll.tagtune.boundedContext.track.controller;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.ApiTrackSearchResult;
import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import com.ll.tagtune.boundedContext.track.service.TrackTagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final TagService tagService;
    private final TrackTagService trackTagService;
    private final Rq rq;

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "artistName", defaultValue = "") String artistName,
            Model model
    ) {
        if (title.isBlank()) rq.historyBack("검색어를 입력해야 합니다.");
        ApiTrackSearchResult rawTracks = (artistName.isBlank()) ?
                SearchEndpoint.searchTrack(title) :
                SearchEndpoint.searchTrack(title, artistName);
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
