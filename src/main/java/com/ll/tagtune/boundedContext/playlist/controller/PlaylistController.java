package com.ll.tagtune.boundedContext.playlist.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.playlist.entity.Playlist;
import com.ll.tagtune.boundedContext.playlist.service.PlaylistService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/playlist") // 액션 URL의 공통 접두어
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final TrackService trackService;
    private final Rq rq;

    public record PlaylistForm(@NotBlank String playlistName) {
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/playlistForm")
    public String makePlaylistForm() {
        return "usr/playlist/playlistForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/playlistForm")
    public String makePlaylist(@Valid PlaylistForm playlistForm) {
        RsData<Playlist> createPlaylist = playlistService.createPlaylist(playlistForm.playlistName, rq.getMember());
        if (createPlaylist.isFail()) {
            // 뒤로가기 하고 거기서 메세지 보여줘
            return rq.historyBack(createPlaylist);
        }
        // 아래 링크로 리다이렉트(302, 이동) 하고 그 페이지에서 메세지 보여줘
        return rq.redirectWithMsg("/playlist/list", createPlaylist);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{playlistId}")
    public String deletePlaylist(@PathVariable Long playlistId) {
        // 멤버의 플레이리스트 id를 가져오기
        RsData<Playlist> deletePlaylist = playlistService.deletePlaylist(playlistId, rq.getMember().getId());
        if (deletePlaylist.isFail()) {
            // 뒤로가기 하고 거기서 메세지 보여줘
            return rq.historyBack(deletePlaylist);
        }

        // 아래 링크로 리다이렉트(302, 이동) 하고 그 페이지에서 메세지 보여줘
        return rq.redirectWithMsg("/playlist/list", deletePlaylist);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String playlists(Model model) {
        //생성된 리스트 보여주기
        List<Playlist> playlists = playlistService.findAllByMemberId(rq.getMember().getId());
        model.addAttribute("playlists", playlists); // 모델에 데이터 추가

        return "usr/playlist/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{playlistId}")
    public String playlistDetail(Model model, @PathVariable Long playlistId) {
        RsData<Playlist> rsPlaylist = playlistService.getPlaylist(playlistId, rq.getMember().getId());
        if (rsPlaylist.isFail()) {
            return rq.historyBack(rsPlaylist);
        }
        model.addAttribute("playlist", rsPlaylist.getData());
        return "usr/playlist/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public String showSearch() {
        return "usr/playlist/search";
    }


    public record PlaylistTrackAddForm(
            @NotNull Long playlistId,
            @NotBlank String trackName,
            @NotBlank String artistName
    ) {
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addTrack")
    public String addTrack(@Valid PlaylistTrackAddForm playlistTrackAddForm) {
        Track track = null;
        // TODO : 영우님 수정
        //Track track = trackService.getOrCreateTrack(playlistTrackAddForm.trackName,playlistTrackAddForm.artistName);
        // 멤버의 플레이리스트 id를 가져오기
        RsData<Playlist> addTrack = playlistService.addTrack(playlistTrackAddForm.playlistId, track);
        if (addTrack.isFail()) {
            // 뒤로가기 하고 거기서 메세지 보여줘
            return rq.historyBack(addTrack);
        }
        //
        // 아래 링크로 리다이렉트(302, 이동) 하고 그 페이지에서 메세지 보여줘
        return rq.redirectWithMsg("/playlist/list", addTrack);
    }
}
