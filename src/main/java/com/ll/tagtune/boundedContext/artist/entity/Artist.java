package com.ll.tagtune.boundedContext.artist.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Artist extends BaseEntity {
    private String artistName;
    private String image;
}
