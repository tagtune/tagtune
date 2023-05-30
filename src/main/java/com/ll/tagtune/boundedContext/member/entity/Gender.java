package com.ll.tagtune.boundedContext.member.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Gender {
    MALE("남성", 1),
    FEMALE("여성", 2);

    private final String value;
    private final Integer code;

    public static Gender findByCode(Integer code) {
        return Arrays.stream(values())
                .filter(a -> a.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not Supported Gender"));
    }
}
