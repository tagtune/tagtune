package com.ll.tagtune.boundedContext.tagVote.repository;

import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;

import java.util.List;

public interface TagVoteRepositoryCustom {
    List<TagVoteCountDTO> getTagVotesCount(final Long memberId);
}
