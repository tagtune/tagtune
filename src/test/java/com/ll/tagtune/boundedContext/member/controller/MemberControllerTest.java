package com.ll.tagtune.boundedContext.member.controller;


import com.ll.tagtune.boundedContext.member.entity.Gender;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // 스프링부트 관련 컴포넌트 테스트할 때 붙여야 함, Ioc 컨테이너 작동시킴
@AutoConfigureMockMvc // http 요청, 응답 테스트
@Transactional // 실제로 테스트에서 발생한 DB 작업이 영구적으로 적용되지 않도록, test + 트랜잭션 => 자동롤백
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
class MemberControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("로그인 폼")
    void t001() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/member/login"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showLogin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        id="btn-login-1"
                        """.stripIndent().trim())));
    }

    @Test
    // @Rollback(value = false) // DB에 흔적이 남는다.
    @DisplayName("로그인 처리")
    void t002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/member/login")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user1")
                        .param("password", "1234")
                )
                .andDo(print());

        // 세션에 접근해서 user 객체를 가져온다.
        MvcResult mvcResult = resultActions.andReturn();
        HttpSession session = mvcResult.getRequest().getSession(false);// 원래 getSession 을 하면, 만약에 없을 경우에 만들어서라도 준다., false 는 없으면 만들지 말라는 뜻
        SecurityContext securityContext = (SecurityContext) (session != null ? Objects.requireNonNull(session).getAttribute("SPRING_SECURITY_CONTEXT") : null);
        User user = (User) Objects.requireNonNull(securityContext).getAuthentication().getPrincipal();

        assertThat(user.getUsername()).isEqualTo("user1");

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/**"));
    }

    @Test
    @DisplayName("회원 추가정보 입력 폼")
    @WithUserDetails("user1")
    void t003() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/member/personalForm"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showPersonalForm"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="radio" name="gender" value="F"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="gender" value="M"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="age" value="10"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="age" value="20"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="age" value="30"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="age" value="40"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="age" value="50"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        id="btn-member-personalForm-1"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("로그인을 안하고 추가 정보 입력 페이지에 접근하면 로그인 페이지로 302")
    void t004() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/member/personalForm"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showPersonalForm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/member/login**"));
    }

    @Test
    @DisplayName("회원 추가정보 입력 폼 처리")
    @WithUserDetails("user1")
    void t005() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/member/personalForm")
                        .with(csrf()) // CSRF 키 생성
                        .param("gender", "F")
                        .param("age", "10")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showPersonalForm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/usr/member/me**"));


        Member member = memberService.findByUsername("user1").orElseThrow();

        assertThat(member.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(member.getAge()).isEqualTo(10);
    }
}