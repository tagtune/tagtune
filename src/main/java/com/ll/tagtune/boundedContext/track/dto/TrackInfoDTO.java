package com.ll.tagtune.boundedContext.track.dto;

import com.ll.tagtune.boundedContext.tag.dto.TagDTO;
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
    private String artistName;
    private String albumName;
    @Builder.Default
    private List<TagDTO> tags = new ArrayList<>();
}
