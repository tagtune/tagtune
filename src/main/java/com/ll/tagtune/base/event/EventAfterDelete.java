package com.ll.tagtune.base.event;

import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterDelete extends ApplicationEvent {
    private final TagComment tagComment;

    public EventAfterDelete(Object source, TagComment tagComment) {
        super(source);
        this.tagComment = tagComment;
    }
}
