package com.ll.tagtune.boundedContext.comment.dto;

import com.ll.tagtune.boundedContext.reply.entity.Reply;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentResponseDTO {
    private Long commentId;
    private Boolean deleteStatus;
    private String content;
    private Long memberId;
    private String memberName;
    private String memberNickName;
    private Integer replyCnt;
    private Long trackId;
    private List<Reply> replies;

    public CommentResponseDTO(
            Long commentId,
            Boolean deleteStatus,
            String content,
            Long memberId,
            String memberName,
            String memberNickName,
            Integer replyCnt,
            Long trackId
    ) {
        this.commentId = commentId;
        this.deleteStatus = deleteStatus;
        this.content = getDeleteStatus() ? "삭제된 댓글입니다." : content;
        this.memberId = getDeleteStatus() ? -1 : memberId;
        this.memberName = getDeleteStatus() ? "" : memberName;
        this.memberNickName = getDeleteStatus() ? "" : memberNickName;
        this.replyCnt = replyCnt;
        this.trackId = trackId;
    }
}