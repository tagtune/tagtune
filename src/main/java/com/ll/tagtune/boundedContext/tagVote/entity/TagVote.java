package com.ll.tagtune.boundedContext.tagVote.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
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
public class TagVote extends BaseEntity {
    @ManyToOne
    private Member member;
    @ManyToOne
    private TrackTag trackTag;
    @Setter
    private Boolean positive;
}
