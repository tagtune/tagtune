package com.ll.tagtune.base.event;

import com.ll.tagtune.boundedContext.comment.entity.Comment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterDelete extends ApplicationEvent {
    private final Comment comment;

    public EventAfterDelete(Object source, Comment comment) {
        super(source);
        this.comment = comment;
    }
}
