package com.ll.tagtune.boundedContext.tagReply.repository;

import com.ll.tagtune.boundedContext.tagReply.dto.TagReplyResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.tagtune.boundedContext.tagReply.entity.QTagReply.tagReply;

@RequiredArgsConstructor
public class TagReplyRepositoryImpl implements TagReplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TagReplyResponseDTO> getReplies(Long tagBoardId) {
        return queryFactory
                .select(
                        Projections.constructor(TagReplyResponseDTO.class,
                                tagReply.id,
                                tagReply.content,
                                tagReply.member.id,
                                tagReply.member.username,
                                tagReply.parent
                        )
                )
                .from(tagReply)
                .orderBy(tagReply.id.desc())
                .fetch();
    }
}
