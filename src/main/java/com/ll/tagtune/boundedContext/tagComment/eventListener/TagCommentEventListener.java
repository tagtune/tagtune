package com.ll.tagtune.boundedContext.tagComment.eventListener;

import com.ll.tagtune.base.event.EventAfterDelete;
import com.ll.tagtune.boundedContext.tagComment.service.TagCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class TagCommentEventListener {
    private final TagCommentService tagCommentService;

    @EventListener
    public void listen(EventAfterDelete event) {
        tagCommentService.runOrphanDelete(event.getTagComment());
    }
}