package com.ll.tagtune.boundedContext.playlist.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.playlist.entity.Playlist;
import com.ll.tagtune.boundedContext.playlist.repository.PlaylistRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class PlaylistSerivceTest {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("Playlist Create Test")
    void t001() throws Exception{
        Member member = Member
                .builder()
                .username("아이유")
                .password("1234")
                .profileImage("아이유표지")
                //.password(passwordEncoder.encode("1234"))
                .build();
        RsData<Playlist> playlist = playlistService.createPlaylist("플레이리스트 1", member);
        assertThat(playlist.getData().getName().equals(member.getUsername()));

    }

    @Test
    @DisplayName("Playlist Delete Test")
    void t002() throws Exception {
    Member member = Member
            .builder()
            .id(3L)
            .username("박준수")
            .password("1234")
            .profileImage("아이유표지")
            //.password(passwordEncoder.encode("1234"))
            .build();
        Long playlistId = playlistService.createPlaylist("플레이리스트 1", member).getData().getId();
        RsData<Playlist> rsData = playlistService.deletePlaylist(playlistId, member.getId());
        assertThat(playlistService.findById(playlistId)).isEmpty();
        assertThat(rsData.isSuccess()).isTrue();
    }
}
