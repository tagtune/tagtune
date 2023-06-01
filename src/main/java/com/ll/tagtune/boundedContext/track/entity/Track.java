package com.ll.tagtune.boundedContext.track.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Track {
    @Id
    private String spotifyId;
    //    @ManyToOne
//    private Artist artist;
//    @ManyToOne
//    private Album album;
    private Integer trackNumber;
    private String title;
    private Integer year;
}
