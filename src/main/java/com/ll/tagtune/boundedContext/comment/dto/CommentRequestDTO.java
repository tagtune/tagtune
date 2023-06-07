package com.ll.tagtune.boundedContext.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDTO {
    private Long memberId;
    private Long parentId;
    private String content;

    public CommentRequestDTO(String content) {
        this.content = content;
    }
}
