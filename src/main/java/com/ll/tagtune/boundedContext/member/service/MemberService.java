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

        return RsData.of("S-1", "회원가입이 완료되었습니다.\n 로그인 후 이용해주세요", member);
    }

    public RsData<Member> updateInfo(Member member, String gender, Integer age) {
        if (!member.equals(rq.getMember())) {
            return RsData.of("F-1", "실패");
        }

        updateGenderAndAge(member, gender, age);

        return RsData.of("S-1", "추가 정보가 등록되었습니다.");
    }

    public void updateGenderAndAge(Member member, String gender, Integer age) {
        member.setGender(gender);
        member.setAge(age);

        memberRepository.save(member); // 여기서 실제로 UPDATE 쿼리 발생
    }
}
