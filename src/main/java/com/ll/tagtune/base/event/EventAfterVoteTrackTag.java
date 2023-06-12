package com.ll.tagtune.base.event;

import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterVoteTrackTag {
    private final TrackTag trackTag;
    private final Integer popularity;
}
