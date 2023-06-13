package com.ll.tagtune.base.event;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterCreateTag {
    private final Tag tag;
}
