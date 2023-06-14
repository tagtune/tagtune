package com.ll.tagtune.boundedContext.playlist.controller;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.playlist.dto.PlaylistDTO;
import com.ll.tagtune.boundedContext.playlist.entity.Playlist;
import com.ll.tagtune.boundedContext.playlist.service.PlaylistService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String makePlaylist() {
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
        RsData<PlaylistDTO> rsPlaylist = playlistService.getPlaylistDTO(playlistId, rq.getMember().getId());
        if (rsPlaylist.isFail()) return rq.historyBack(rsPlaylist);
        model.addAttribute("playlist", rsPlaylist.getData());

        return "usr/playlist/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/search")
    public String showSearch(
            @PathVariable Long id,
            @RequestParam(value = "trackName", defaultValue = "") String trackName,
            @RequestParam(value = "artistName", defaultValue = "") String artistName,
            Model model
    ) {
        model.addAttribute("playlistId", id);
        if (!trackName.isBlank()) {
            if (artistName.isBlank()) rq.historyBack("트랙 제목을 입력해야합니다.");
            List<TrackSearchDTO> rawTracks = (artistName.isBlank() ?
                    SearchEndpoint.searchTrack(trackName) :
                    SearchEndpoint.searchTrack(trackName, artistName))
                    .getTracks();
            model.addAttribute("tracks", rawTracks);
        }

        return "usr/playlist/search";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/addTrack")
    public String addTrack(@PathVariable Long id, TrackSearchDTO trackSearchDTO) {
        if (trackSearchDTO.name.isBlank() || trackSearchDTO.artist.isBlank())
            return rq.historyBack("잘못된 데이터입니다.");

        final Optional<Track> oTrack = trackService.setTrackInfo(trackSearchDTO);
        if (oTrack.isEmpty()) return rq.historyBack("잘못된 데이터입니다.");

        RsData<Playlist> rsPlaylist = playlistService.addTrack(rq.getMember().getId(), id, oTrack.get());
        if (rsPlaylist.isFail()) return rq.historyBack(rsPlaylist);

        return rq.redirectWithMsg("/playlist/detail/" + id, rsPlaylist);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/deleteTrack/{trackId}")
    public String addTrack(@PathVariable Long id, @PathVariable Long trackId) {
        RsData<Playlist> rsPlaylist = playlistService.deleteTrack(rq.getMember().getId(), id, trackId);
        if (rsPlaylist.isFail()) return rq.historyBack(rsPlaylist);

        return rq.redirectWithMsg("/playlist/detail/" + id, rsPlaylist);
    }
}
