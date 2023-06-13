package com.ll.tagtune.boundedContext.reply.repository;

import com.ll.tagtune.boundedContext.reply.dto.ReplyDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.tagtune.boundedContext.reply.entity.QReply.reply;

@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReplyDTO> getReplies(Long commentId) {
        return queryFactory
                .select(
                        Projections.constructor(ReplyDTO.class,
                                reply.id,
                                reply.content,
                                reply.parent.id,
                                reply.member
                        )
                )
                .from(reply)
                .where(
                        reply.parent.id.eq(commentId)
                )
                .orderBy(reply.id.asc())
                .fetch();
    }
}
