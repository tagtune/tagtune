package com.ll.tagtune.boundedContext.lyric.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.track.entity.Track;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Lyric extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Language language;
    private String content;
    @ManyToOne
    Track track;
}
