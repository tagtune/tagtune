package com.ll.tagtune.boundedContext.music.entity;

import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Music {
    @Id
    private String spotifyId;
    @ManyToOne
    private Artist artist;
    @ManyToOne
    private Album album;
    private Integer trackNumber;
    private String title;
    private Integer year;
}
