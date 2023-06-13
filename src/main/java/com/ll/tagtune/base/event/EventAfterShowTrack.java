package com.ll.tagtune.base.event;

import com.ll.tagtune.boundedContext.lyric.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterShowTrack {
    private final Long trackId;
    private final Language language;
}
