package com.ll.tagtune.boundedContext.tagComment.repository;

import com.ll.tagtune.boundedContext.tagComment.dto.TagCommentResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.tagtune.boundedContext.tagComment.entity.QTagComment.tagComment;

@RequiredArgsConstructor
public class TagCommentRepositoryImpl implements TagCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TagCommentResponseDTO> getComments(Long tagBoardId) {
        return queryFactory
                .select(
                        Projections.constructor(TagCommentResponseDTO.class,
                                tagComment.id,
                                tagComment.deleteStatus,
                                tagComment.content,
                                tagComment.member.id,
                                tagComment.member.username,
                                tagComment.tagReplies.size(),
                                tagComment.tagBoard.id
                        )
                )
                .from(tagComment)
                .where(
                        tagComment.tagBoard.id.eq(tagBoardId)
                                .and(
                                        tagComment.deleteStatus.eq(false)
                                                .or(
                                                        tagComment.deleteStatus.eq(true)
                                                                .and(tagComment.tagReplies.isNotEmpty())
                                                )
                                )

                )
                .orderBy(tagComment.id.desc())
                .fetch();
    }
}