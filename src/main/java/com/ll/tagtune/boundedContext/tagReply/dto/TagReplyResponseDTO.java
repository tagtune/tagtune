package com.ll.tagtune.boundedContext.tagReply.dto;

import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import lombok.Getter;

@Getter
public class TagReplyResponseDTO {
    private Long replyId;
    private String content;
    private Long memberId;
    private String memberName;
    private TagComment parent;

    public TagReplyResponseDTO(
            Long replyId,
            String content,
            Long memberId,
            String memberName,
            TagComment parent
    ) {
        this.replyId = replyId;
        this.content = content;
        this.memberId = memberId;
        this.memberName = memberName;
        this.parent = parent;
    }

}
