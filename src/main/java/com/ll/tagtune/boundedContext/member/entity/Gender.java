package com.ll.tagtune.boundedContext.member.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남성", 1),
    FEMALE("여성", 2);

    private final String value;
    private final Integer code;

    Gender(String value, Integer code) {
        this.value = value;
        this.code = code;
    }
}
