package com.ll.tagtune.base.lastfm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrackSearchDTO {
    public String name;
    public String artist;
}
