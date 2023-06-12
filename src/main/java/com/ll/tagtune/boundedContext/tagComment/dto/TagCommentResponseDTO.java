package com.ll.tagtune.boundedContext.tagComment.dto;

import lombok.Getter;

@Getter
public class TagCommentResponseDTO {
    private Long commentId;
    private Boolean deleteStatus;
    private String content;
    private Long memberId;
    private String memberName;
    private Integer replyCnt;
    private Long TagBoardId;

    public TagCommentResponseDTO(
            Long commentId,
            Boolean deleteStatus,
            String content,
            Long memberId,
            String memberName,
            Integer replyCnt,
            Long tagBoardId
    ) {
        this.commentId = commentId;
        this.deleteStatus = deleteStatus;
        this.content = getDeleteStatus() ? "삭제된 댓글입니다." : content;
        this.memberId = getDeleteStatus() ? -1 : memberId;
        this.memberName = getDeleteStatus() ? "" : memberName;
        this.replyCnt = replyCnt;
        this.TagBoardId = tagBoardId;
    }
}