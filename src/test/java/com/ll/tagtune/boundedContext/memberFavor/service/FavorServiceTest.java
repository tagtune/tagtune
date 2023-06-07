package com.ll.tagtune.boundedContext.memberFavor.service;

import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorBase;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class FavorServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private TagService tagService;
    @Autowired
    private FavorService favorService;

    @Test
    @DisplayName("Create Favor Test")
    void t001() throws Exception {
        final Member member = memberService.findByUsername("user1")
                .orElseThrow(() -> new UsernameNotFoundException(""));
        final Tag tag = tagService.createTag("긮슝");

        FavorTag favor = favorService.create(member, tag);

        assertThat(favor.getData()).isEqualTo(tag);
    }

    @Test
    @DisplayName("Get Favor List Test")
    void t002() throws Exception {
        final Member member = memberService.findByUsername("user1")
                .orElseThrow(() -> new UsernameNotFoundException(""));
        final Artist artist = artistService.createArtist("계릫");
        final Album album = albumService.createAlbum("흎폼", "0000");
        final Tag tag = tagService.createTag("뀴유");

        FavorBase[] favors = {
                favorService.create(member, artist),
                favorService.create(member, album),
                favorService.create(member, tag)
        };

        assertThat(favors).isNotEmpty();
        assertThat(Arrays.stream(favors).map(FavorBase::getData).filter(o -> o.equals(artist))).isNotEmpty();
        assertThat(Arrays.stream(favors).map(FavorBase::getData).filter(o -> o.equals(album))).isNotEmpty();
        assertThat(Arrays.stream(favors).map(FavorBase::getData).filter(o -> o.equals(tag))).isNotEmpty();

        FavorBase[] favorBases = favorService.getFavorTags(member.getId()).toArray(FavorBase[]::new);

        assertThat(favors).
    }
}
