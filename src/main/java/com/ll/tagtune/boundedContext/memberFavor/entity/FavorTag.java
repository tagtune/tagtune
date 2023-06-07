package com.ll.tagtune.boundedContext.memberFavor.entity;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
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
public class FavorTag extends FavorBase<Tag> {
}
