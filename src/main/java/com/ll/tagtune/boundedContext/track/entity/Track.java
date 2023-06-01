package com.ll.tagtune.boundedContext.track.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String spotifyId;
    //    @ManyToOne
//    private Artist artist;
//    @ManyToOne
//    private Album album;
    private Integer trackNumber;
    private String title;
//    @ManyToOne
//    private ReleaseYear releaseYear;
}
