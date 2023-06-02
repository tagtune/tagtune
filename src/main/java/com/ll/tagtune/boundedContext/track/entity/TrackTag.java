package com.ll.tagtune.boundedContext.track.entity;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
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
    @Builder.Default
    private Long voteCount = 0L;
}
