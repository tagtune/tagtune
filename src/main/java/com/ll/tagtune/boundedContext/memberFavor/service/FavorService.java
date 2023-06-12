package com.ll.tagtune.boundedContext.memberFavor.service;

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
    public List<FavorTag> getFavorTags(final Long memberId) {
        return favorTagRepository.findAllByMember_Id(memberId);
    }

    public FavorTag create(final Member member, final Tag tag) {
        FavorTag favorTag = FavorTag.builder()
                .member(member)
                .tag(tag)
                .build();

        favorTagRepository.save(favorTag);

        return favorTag;
    }
}
