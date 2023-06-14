package com.ll.tagtune.boundedContext.playlist.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaylistTrackDTO {
    private Long id;
    private String name;
    private String artist;
}
