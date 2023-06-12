package com.ll.tagtune.boundedContext.tagReply.repository;

import com.ll.tagtune.boundedContext.tagReply.dto.TagReplyResponseDTO;

import java.util.List;

public interface TagReplyRepositoryCustom {
    List<TagReplyResponseDTO> getReplies(Long trackId);
}
