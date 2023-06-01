package com.ll.tagtune.boundedContext.member.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Gender {
    MALE("남성", "M"),
    FEMALE("여성", "F");

    private final String value;
    private final String code;

    public static Gender findByCode(String code) {
        return Arrays.stream(values())
                .filter(a -> a.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not Supported Gender"));
    }
}
