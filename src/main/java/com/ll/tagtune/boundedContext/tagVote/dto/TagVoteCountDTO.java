package com.ll.tagtune.boundedContext.tagVote.dto;


import lombok.Data;

@Data
public class TagVoteCountDTO {
    private String tagName;
    private Long voteCount;

    public TagVoteCountDTO(String tagName, Long voteCount) {
        this.tagName = tagName;
        this.voteCount = voteCount;
    }
}
