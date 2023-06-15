package com.ll.tagtune.base.lastfm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Track 의 name 과 artist 를 가지는 DTO 입니다.
 * <p>
 * Lastfm 의 검색 응답과, 사용자의 검색 form 에 사용합니다.
 * 검색 응답의 중복을 제거하기 위해 Set 을 사용할 수 있습니다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackSearchDTO {
    private String name;
    private String artist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackSearchDTO that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(artist, that.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist);
    }
}
