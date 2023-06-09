package com.ll.tagtune.boundedContext.memberFavor.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import com.ll.tagtune.boundedContext.memberFavor.repository.FavorTagRepository;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavorService {
    private final FavorTagRepository favorTagRepository;

    @Transactional(readOnly = true)
    public RsData<FavorTag> getFavorTag(final Long id) {
        return favorTagRepository.findById(id)
                .map(RsData::successOf)
                .orElseGet(() -> RsData.of("F-1", "해당하는 데이터가 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<FavorTag> getFavorTags(final Long memberId) {
        return favorTagRepository.findAllByMember_Id(memberId);
    }

    @Transactional(readOnly = true)
    public List<FavorTag> getFavorTagsTop3(final Long memberId) {
        return favorTagRepository.findTop3ByMember_idOrderByIdDesc(memberId);
    }

    public FavorTag create(final Member member, final Tag tag) {
        FavorTag favorTag = FavorTag.builder()
                .member(member)
                .tag(tag)
                .build();

        favorTagRepository.save(favorTag);

        return favorTag;
    }

    public RsData<Void> delete(final Member member, final Long id) {
        RsData<FavorTag> rsData = getFavorTag(id);
        if (rsData.isFail()) return RsData.of("F-1", "해당하는 선호태그가 없습니다.");
        if (!rsData.getData().getMember().getId().equals(member.getId())) return RsData.of("F-2", "잘못된 접근입니다.");
        // 태그의 멤버와 제공된 멤버의 id가 일치하는지 검사
        favorTagRepository.delete(rsData.getData());

        return RsData.of("S-1", "삭제가 완료되었습니다.");
    }
}
