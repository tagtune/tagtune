package com.ll.tagtune.boundedContext.playlist.controller;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.playlist.entity.Playlist;
import com.ll.tagtune.boundedContext.playlist.repository.PlaylistRepository;
import com.ll.tagtune.boundedContext.playlist.service.PlaylistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usr/playlist") // 액션 URL의 공통 접두어
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistRepository playlistRepository;
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
    public String makePlaylistForm(@Valid PlaylistForm playlistForm) {
        RsData<Playlist> createPlaylist = playlistService.createPlaylist(playlistForm.playlistName,rq.getMember());
        if (createPlaylist.isFail()) {
            // 뒤로가기 하고 거기서 메세지 보여줘
            return rq.historyBack(createPlaylist);
        }
        // 아래 링크로 리다이렉트(302, 이동) 하고 그 페이지에서 메세지 보여줘
        return rq.redirectWithMsg("/usr/playlist/list", createPlaylist);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{playlistId}")
    public String deletePlaylistForm(@PathVariable Long playlistId){
        // 멤버의 플레이리스트 id를 가져오기
        RsData<Playlist>deletePlaylist = playlistService.deletePlaylist(playlistId, rq.getMember().getId());
        if (deletePlaylist.isFail()) {
            // 뒤로가기 하고 거기서 메세지 보여줘
            return rq.historyBack(deletePlaylist);
        }

        // 아래 링크로 리다이렉트(302, 이동) 하고 그 페이지에서 메세지 보여줘
        return rq.redirectWithMsg("/usr/playlist/list", deletePlaylist);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String playlistView(Model model) {
        //생성된 리스트 보여주기
        List<Playlist> playlistByMemberId = playlistService.findAllByMemberId(rq.getMember().getId());
        model.addAttribute("playlistByMemberId", playlistByMemberId); // 모델에 데이터 추가

        return "usr/playlist/list";
    }
}
