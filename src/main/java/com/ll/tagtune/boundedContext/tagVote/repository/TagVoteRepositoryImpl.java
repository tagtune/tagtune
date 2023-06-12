package com.ll.tagtune.boundedContext.tagVote.repository;

import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.tagtune.boundedContext.tagVote.entity.QTagVote.tagVote;
import static com.ll.tagtune.boundedContext.track.entity.QTrackTag.trackTag;

@RequiredArgsConstructor
public class TagVoteRepositoryImpl implements TagVoteRepositoryCustom {
    private static final Integer TOP_COUNT = 3;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TagVoteCountDTO> getTagVotesCount(Long memberId) {
        return jpaQueryFactory.select(Projections.constructor(TagVoteCountDTO.class,
                        trackTag.tag.tagName,
                        tagVote.count()
                ))
                .from(tagVote)
                .join(tagVote.trackTag, trackTag)
                .where(tagVote.member.id.eq(memberId), tagVote.positive.isTrue())
                .groupBy(trackTag.tag.tagName)
                .orderBy(tagVote.count().desc())
                .limit(TOP_COUNT)
                .fetch();
    }
}
