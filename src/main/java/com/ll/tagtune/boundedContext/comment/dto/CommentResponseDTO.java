package com.ll.tagtune.boundedContext.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDTO {
    private Long commentId;
    private Boolean deleteStatus;
    private String content;
    private Long memberId;
    private String memberName;
    private Integer replyCnt;

    public CommentResponseDTO(
            Long commentId,
            Boolean deleteStatus,
            String content,
            Long memberId,
            String memberName,
            Integer replyCnt
    ) {
        this.commentId = commentId;
        this.deleteStatus = deleteStatus;
        this.content = getDeleteStatus() ? "삭제된 댓글입니다." : content;
        this.memberId = getDeleteStatus() ? -1 : memberId;
        this.memberName = getDeleteStatus() ? "" : memberName;
        this.replyCnt = replyCnt;
    }
}
