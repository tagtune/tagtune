package com.ll.tagtune.boundedContext.playlist.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.track.entity.Track;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Playlist extends BaseEntity {
    @Column(nullable = false)
    private String name;
    /**
     * 한 멤버가 여러개의 플레이리스트를 가질 수 있다.
     */
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
    @OneToMany
    @OrderBy("id desc")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ToString.Exclude
    @Builder.Default
    private List<Track> tracks = new ArrayList<>();
}
