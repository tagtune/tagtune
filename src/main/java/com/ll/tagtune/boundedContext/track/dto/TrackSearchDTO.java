package com.ll.tagtune.boundedContext.track.dto;

import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import lombok.Builder;
import lombok.Data;

/**
 *  Track 의 title, Artist 를 저장 하는 DTO 입니다.
 *  <p>
 *  Track 의 last.fm API 가 Album, Tag 정보를 가져오지 않아 사용합니다
 */
@Data
@Builder
public class TrackSearchDTO {
    private String title;
    private ArtistDTO artistDTO;
}
