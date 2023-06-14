package com.ll.tagtune.boundedContext.lyric.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.lyric.ut.LanguageConvertor;
import com.ll.tagtune.boundedContext.track.entity.Track;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
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
    @Convert(converter = LanguageConvertor.class)
    private Language language;
    @Builder.Default
    @Column(length = 5000)
    private String content = "가사가 비어있습니다.";
    @ManyToOne
    private Track track;
}
