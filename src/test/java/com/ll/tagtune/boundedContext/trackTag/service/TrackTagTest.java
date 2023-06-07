package com.ll.tagtune.boundedContext.trackTag.service;

import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.tagVote.entity.TagVote;
import com.ll.tagtune.boundedContext.tagVote.service.TagVoteService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import com.ll.tagtune.boundedContext.track.service.TrackTagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TrackTagTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TrackTagService trackTagService;
    @Autowired
    private TagVoteService tagVoteService;

    @Test
    @DisplayName("Track Tag Create Test")
    void t001() throws Exception {
        final Artist artist = artistService.createArtist("곛잔ㅅ");
        final Track track = trackService.createTrack("힇기", artist);
        final Tag tag = tagService.createTag("PP-12");

        TrackTag trackTag = trackTagService.connect(track, tag);

        assertThat(trackTag.getTag().getId()).isEqualTo(tag.getId());
    }

    @Test
    @DisplayName("TrackTag Vote Test")
    void t002() throws Exception {
        final Member member = memberService.findByUsername("user1")
                .orElseThrow(() -> new UsernameNotFoundException(""));
        final Artist artist = artistService.createArtist("잏귯빻");
        final Track track = trackService.createTrack("혚뒭", artist);
        final Tag tag = tagService.createTag("SUPE__-+1");
        final TrackTag trackTag = trackTagService.connect(track, tag);
        final Boolean memberVote = Boolean.FALSE;
        TagVote tagVote = tagVoteService.vote(memberVote, member, trackTag);

        assertThat(tagVote.getPositive()).isEqualTo(memberVote);
    }
}
