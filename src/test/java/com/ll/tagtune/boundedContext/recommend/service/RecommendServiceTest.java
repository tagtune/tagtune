package com.ll.tagtune.boundedContext.recommend.service;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class RecommendServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private RecommendService recommendService;

    private Member member;

    @BeforeEach
    void beforeEach() {
        member = memberService.findByUsername("user1").orElseThrow();
    }

    @Test
    @DisplayName("get Favor Recommends")
    void t001() throws Exception {
        List<TrackInfoDTO> result = recommendService.getFavoriteList(member);
        // debug
        // result.forEach(System.out::println);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("get Personal Recommends")
    void t002() throws Exception {
        List<TrackInfoDTO> result = recommendService.getPersonalList(member);
        // debug
        // result.forEach(System.out::println);
        assertThat(result).isNotEmpty();
    }
}
