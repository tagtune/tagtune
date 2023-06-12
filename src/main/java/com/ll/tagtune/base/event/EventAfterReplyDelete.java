package com.ll.tagtune.base.event;

import com.ll.tagtune.boundedContext.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@AllArgsConstructor
public class EventAfterReplyDelete{
    private final Comment comment;
}
