package com.ll.tagtune.boundedContext.album.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;

import jakarta.persistence.Column;
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
public class Album extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String image;
}
