package com.ll.tagtune.boundedContext.member.service;

import com.ll.tagtune.base.rq.Rq;
import com.ll.tagtune.base.rsData.RsData;
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
        return join("TAGTUNE", username, password);
    }

    public RsData<Member> join(String providerTypeCode, String username, String password) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .providerTypeCode(providerTypeCode)
                .password(passwordEncoder.encode(password))
                .build();

        memberRepository.save(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.\n 로그인 후 이용해주세요", member);
    }

    public RsData<Member> updateInfo(Member member, String nickName) {
        if (!member.getId().equals(rq.getMember().getId())) return RsData.of("F-1", "실패");
        updateNickName(member, nickName);

        return RsData.of("S-1", "추가 정보가 등록되었습니다.");
    }

    public void updateNickName(Member member, String nickName) {
        member.setNickName(nickName);

        memberRepository.save(member); // 여기서 실제로 UPDATE 쿼리 발생
    }

    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String username) {
        Optional<Member> opMember = findByUsername(username);

        return opMember.map(member -> RsData.of("S-1", "로그인 되었습니다.", member))
                .orElseGet(() -> join(providerTypeCode, username, ""));
    }
}
