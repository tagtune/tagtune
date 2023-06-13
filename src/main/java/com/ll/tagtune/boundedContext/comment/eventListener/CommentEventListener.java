package com.ll.tagtune.boundedContext.comment.eventListener;
import com.ll.tagtune.base.event.EventAfterReplyDelete;
import com.ll.tagtune.boundedContext.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class CommentEventListener {
    private final CommentService commentService;
    @EventListener
    public void listen(EventAfterReplyDelete event) {
        commentService.runOrphanDelete(event.getComment());
    }
}