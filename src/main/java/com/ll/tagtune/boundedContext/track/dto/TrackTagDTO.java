package com.ll.tagtune.boundedContext.track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrackTagDTO {
    private Long id;
    private String tagName;
    private Long voteCount;
}
