package com.ll.tagtune.boundedContext.recommend.service;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

//    @Test
//    @DisplayName("get Favor Recommends")
//    void t001() throws Exception {
//        List<TrackInfoDTO> result = recommendService.getFavoriteList(member);
//        // debug
//        result.forEach(System.out::println);
//        assertThat(result).isNotEmpty();
//    }
}
