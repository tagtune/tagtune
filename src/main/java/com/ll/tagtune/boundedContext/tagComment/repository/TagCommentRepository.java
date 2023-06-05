package com.ll.tagtune.boundedContext.tagComment.repository;

import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagCommentRepository extends JpaRepository<TagComment, Long> {
}
