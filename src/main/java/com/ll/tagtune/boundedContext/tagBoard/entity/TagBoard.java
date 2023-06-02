package com.ll.tagtune.boundedContext.tagBoard.entity;

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
public class TagBoard extends BaseEntity {
    private String tagBoardName;
}
