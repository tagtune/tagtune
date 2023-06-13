package com.ll.tagtune.boundedContext.reply.repository;

import com.ll.tagtune.boundedContext.reply.dto.ReplyDTO;

import java.util.List;

public interface ReplyRepositoryCustom {
    List<ReplyDTO> getReplies(Long commentId);
}
