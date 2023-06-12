package com.ll.tagtune.boundedContext.tagComment.repository;

import com.ll.tagtune.boundedContext.tagComment.dto.TagCommentResponseDTO;

import java.util.List;

public interface TagCommentRepositoryCustom {
    List<TagCommentResponseDTO> getComments(Long trackId);
}