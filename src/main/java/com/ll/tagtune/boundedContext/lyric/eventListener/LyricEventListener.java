package com.ll.tagtune.boundedContext.lyric.eventListener;

import com.ll.tagtune.base.event.EventAfterShowTrack;
import com.ll.tagtune.boundedContext.lyric.service.LyricService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class LyricEventListener {
    private final LyricService lyricService;

    @EventListener
    public void listen(EventAfterShowTrack eventAfterShowTrack) {
        lyricService.showLyric(eventAfterShowTrack.getTrackId(), eventAfterShowTrack.getLanguage());
    }
}
