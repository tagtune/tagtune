package com.ll.tagtune.boundedContext.comment.repository;

import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.tagtune.boundedContext.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDTO> getComments(Long trackId) {
        return queryFactory
                .select(
                        Projections.constructor(CommentResponseDTO.class,
                                comment.id,
                                comment.deleteStatus,
                                comment.content,
                                comment.member.id,
                                comment.member.username,
                                comment.member.nickName,
                                comment.replies.size(),
                                comment.track.id
                        )
                )
                .from(comment)
                .where(
                        comment.track.id.eq(trackId)
                                .and(
                                        comment.deleteStatus.eq(false)
                                                .or(
                                                        comment.deleteStatus.eq(true)
                                                                .and(comment.replies.isNotEmpty())
                                                )
                                )

                )
                .orderBy(comment.id.desc())
                .fetch();
    }
}
