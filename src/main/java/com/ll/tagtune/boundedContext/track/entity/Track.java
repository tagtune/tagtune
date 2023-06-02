package com.ll.tagtune.boundedContext.track.entity;

import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.releaseYear.entity.ReleaseYear;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

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
    private String title;
    @ManyToOne
    private Artist artist;
    @ManyToOne
    private Album album;
    @ManyToOne
    private ReleaseYear releaseYear;
    @OneToMany(mappedBy = "track", cascade = {CascadeType.ALL})
    @OrderBy("voteCount desc")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ToString.Exclude
    @Builder.Default
    private List<TrackTag> tags = new ArrayList<>();
}
