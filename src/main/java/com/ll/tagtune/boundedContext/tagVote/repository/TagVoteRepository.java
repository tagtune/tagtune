package com.ll.tagtune.boundedContext.tagVote.repository;

import com.ll.tagtune.boundedContext.tagVote.entity.TagVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagVoteRepository extends JpaRepository<TagVote, Long> {
    Optional<TagVote> findByMember_IdAndTrackTag_Id(Long memberId, Long trackTagId);
}
