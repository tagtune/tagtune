package com.ll.tagtune.boundedContext.tagBoard.eventListener;

import com.ll.tagtune.base.event.EventAfterCreateTag;
import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class TagBoardEventListener {
    private final TagBoardService tagBoardService;

    @EventListener
    public void listen(EventAfterCreateTag eventAfterCreateTag) {
        tagBoardService.create(eventAfterCreateTag.getTag());
    }
}
