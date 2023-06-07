package com.ll.tagtune.boundedContext.comment.dto;

import com.ll.tagtune.boundedContext.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private Member writer;
    private List<CommentResponseDTO> children = new ArrayList<>();

    public CommentResponseDTO(Long id, String content, Member writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }
}
