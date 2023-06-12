package com.ll.tagtune.boundedContext.memberFavor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavorTagDTO {
    private Long id;
    private Long tagId;
    private String name;
}
