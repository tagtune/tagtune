package com.ll.tagtune.boundedContext.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PlaylistDTO {
    private Long id;
    private String name;
    private List<PlaylistTrackDTO> tracks;
}
