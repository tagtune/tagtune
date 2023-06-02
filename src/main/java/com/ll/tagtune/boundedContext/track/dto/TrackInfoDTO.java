package com.ll.tagtune.boundedContext.track.dto;

import com.ll.tagtune.boundedContext.album.dto.AlbumDTO;
import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import com.ll.tagtune.boundedContext.tag.dto.TagDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TrackInfoDTO {
    private String title;
    private ArtistDTO artistDTO;
    private AlbumDTO albumDTO;
    @Builder.Default
    private List<TagDTO> tags = new ArrayList<>();
}
