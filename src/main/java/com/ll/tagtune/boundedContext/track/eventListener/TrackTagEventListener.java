package com.ll.tagtune.boundedContext.track.eventListener;

import com.ll.tagtune.base.event.EventAfterVoteTrackTag;
import com.ll.tagtune.boundedContext.track.service.TrackTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class TrackTagEventListener {
    private final TrackTagService trackTagService;

    @EventListener
    public void listen(EventAfterVoteTrackTag eventAfterVoteTrackTag) {
        trackTagService.update(eventAfterVoteTrackTag.getTrackTag(), eventAfterVoteTrackTag.getPopularity());
    }
}
