package com.ll.tagtune.base.spotify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SpotifyType {
    ARTIST("artist"),
    ALBUM("album"),
    TRACK("track"),
    PLAYLIST("playlist");

    @Getter
    private final String value;

    public static SpotifyType of(final String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not Supported SpotifyType"));
    }
}
