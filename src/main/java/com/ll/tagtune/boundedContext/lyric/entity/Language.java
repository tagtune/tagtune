package com.ll.tagtune.boundedContext.lyric.entity;

import lombok.Getter;

@Getter
public enum Language {
    KOREAN("Kor"),
    ENGLISH("Eng");
    private final String name;
    Language(String name) {
        this.name = name;
    }
}
