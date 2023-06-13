package com.ll.tagtune.boundedContext.track.dto;

import lombok.Data;

@Data
public class TrackTagStatusDTO {
    private Long id;
    private Long tagId;
    private String tagName;
    private Integer voteCount;
    private Boolean positive;

    public TrackTagStatusDTO(Long id, Long tagId, String tagName, Integer voteCount) {
        this.id = id;
        this.tagId = tagId;
        this.tagName = tagName;
        this.voteCount = voteCount;
    }

    public TrackTagStatusDTO(Long id, Long tagId, String tagName, Integer voteCount, Boolean positive) {
        this.id = id;
        this.tagId = tagId;
        this.tagName = tagName;
        this.voteCount = voteCount;
        this.positive = positive;
    }
}
