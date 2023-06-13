package com.ll.tagtune.boundedContext.comment.repository;

import com.ll.tagtune.boundedContext.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
