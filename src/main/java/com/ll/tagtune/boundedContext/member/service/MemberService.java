package com.ll.tagtune.boundedContext.member.service;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.member.entity.Gender;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final Rq rq;

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public RsData<Member> join(String username, String password) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .profileImage(username)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }

    // member : 현재 로그인한 회원
    // username : 입력한 본인 인스타 username
    // gender : 입력한 본인의 성별
    public RsData<Member> connect(Member member, Gender gender, Integer age) {
        updateGenderAndAge(member, gender, age);

        return RsData.of("S-1", "성공");
    }
    public void updateGenderAndAge(Member member, Gender gender, Integer age) {
        Member
                .builder()
                .gender(gender)
                .age(age)
                .build();

        memberRepository.save(member); // 여기서 실제로 UPDATE 쿼리 발생
    }
}
