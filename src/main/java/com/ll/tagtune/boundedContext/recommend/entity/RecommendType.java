package com.ll.tagtune.boundedContext.recommend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RecommendType {
    TRENDING(0),
    FAVORITE(1),
    PERSONAL(2);

    private final Integer code;

    public static RecommendType codeOf(final Integer code) {
        return Arrays.stream(RecommendType.values())
                .filter(o -> o.getCode().equals(code))
                .findFirst()
                .orElse(TRENDING);
    }
}
