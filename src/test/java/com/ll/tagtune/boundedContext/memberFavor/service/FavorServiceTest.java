package com.ll.tagtune.boundedContext.memberFavor.service;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
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

        assertThat(favor.getTag()).isEqualTo(tag);
    }

    @Test
    @DisplayName("Get Favor List Test")
    void t002() throws Exception {
        final Member member = memberService.findByUsername("user1")
                .orElseThrow(() -> new UsernameNotFoundException(""));
        final Tag tag = tagService.createTag("뀴유");

        FavorTag[] favors = {
                favorService.create(member, tag)
        };

        assertThat(favors).isNotEmpty();
        assertThat(Arrays.stream(favors).map(FavorTag::getTag).filter(o -> o.equals(tag))).isNotEmpty();

        FavorTag[] favorTags = favorService.getFavorTags(member.getId()).toArray(FavorTag[]::new);

        // assert favor and favorBase is equal
    }
}
