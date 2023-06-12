package com.ll.tagtune.boundedContext.comment.repository;

import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseDTO> getComments(Long trackId);
}
