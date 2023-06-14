package com.ll.tagtune.boundedContext.lyric.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Language {
    KOREAN("Kor"),
    ENGLISH("Eng");
    private final String name;

    public static Language nameOf(final String name) {
        return Arrays.stream(Language.values())
                .filter(e -> e.getName().equals(name))
                .findFirst().orElseThrow();
    }
}
