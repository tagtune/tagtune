package com.ll.tagtune.boundedContext.music.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Music extends BaseEntity {
    @ManyToOne
    private Artist artist;
    @ManyToOne
    private Album album;
    private Integer trackNumber;
    private String title;
    private Integer year;
}
