package com.ll.tagtune.boundedContext.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDTO {
    private Long id;
    private String tagName;
    private Long popularity;
}
