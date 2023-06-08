package com.ll.tagtune.boundedContext.track.dto;

import com.ll.tagtune.boundedContext.album.dto.AlbumDTO;
import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * track 의 title, Artist, Album 를 저장 하는 DTO 입니다.
 */
@Data
@Builder
public class TrackInfoDTO {
    private String title;
    private ArtistDTO artistDTO;
    private AlbumDTO albumDTO;
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
