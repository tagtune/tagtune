package com.ll.tagtune.boundedContext.reply.dto;

import com.ll.tagtune.boundedContext.member.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDTO {
    private Long id;
    private String content;
    private Long commentId;
    private Member member;

    public ReplyDTO(Long id, String content, Long commentId, Member member) {
        this.id = id;
        this.content = content;
        this.commentId = commentId;
        this.member = member;
    }
}
