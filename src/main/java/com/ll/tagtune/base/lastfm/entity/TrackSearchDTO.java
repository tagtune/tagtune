package com.ll.tagtune.base.lastfm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackSearchDTO {
    public String name;
    public String artist;

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
