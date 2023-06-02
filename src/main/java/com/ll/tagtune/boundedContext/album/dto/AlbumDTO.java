package com.ll.tagtune.boundedContext.album.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbumDTO {
    private String name;
    private String image;
}
