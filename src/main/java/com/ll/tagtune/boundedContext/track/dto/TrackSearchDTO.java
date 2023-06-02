package com.ll.tagtune.boundedContext.track.dto;

import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackSearchDTO {
    private String title;
    private ArtistDTO artistDTO;
}
