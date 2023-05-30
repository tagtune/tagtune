package com.ll.tagtune.boundedContext;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("Member Join Test")
    void t001() throws Exception {
        final String userName = "cheolSoo";
        final String password = "1234";

        Member member = memberService.join(userName, password).getData();
        assertThat(member.getProfileImage()).isEqualTo(userName);
    }
}
