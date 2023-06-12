package com.ll.tagtune.boundedContext.track.entity;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tagVote.entity.TagVote;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrackTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Track track;
    @ManyToOne
    private Tag tag;
    @OneToMany(mappedBy = "trackTag", cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ToString.Exclude
    @Builder.Default
    private List<TagVote> tagVotes = new ArrayList<>();
    @Builder.Default
    private Integer popularity = 0;
}
