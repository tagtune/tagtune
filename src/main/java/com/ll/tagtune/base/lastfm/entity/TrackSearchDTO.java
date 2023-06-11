package com.ll.tagtune.base.lastfm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackSearchDTO {
    public String name;
    public String artist;
}
