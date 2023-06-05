package com.ll.tagtune.boundedContext.playlist.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
    private Member member;

//    @OneToMany
//    private List<Track> tracks;
}
