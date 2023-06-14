package com.ll.tagtune.boundedContext.lyric.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LyricDTO {
    private String language;
    @Builder.Default
    private String content = "가사가 비어있습니다.";
    private String title;
}
