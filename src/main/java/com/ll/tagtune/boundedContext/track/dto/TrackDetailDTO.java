package com.ll.tagtune.boundedContext.track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackDetailDTO {
    private Long id;
    private String title;
    private Long artistId;
    private String artistName;
    private Long albumId;
    private String albumName;
    private List<TrackTagDTO> tags;

    public TrackDetailDTO(
            Long id,
            String title,
            Long artistId,
            String artistName,
            Long albumId,
            String albumName
    ) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.artistName = artistName;
        this.albumId = albumId;
        this.albumName = albumName;
    }
}
