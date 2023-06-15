package com.ll.tagtune.boundedContext.reply.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.member.entity.Member;
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
public class Reply extends BaseEntity {
    private String content;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Comment parent;
}
