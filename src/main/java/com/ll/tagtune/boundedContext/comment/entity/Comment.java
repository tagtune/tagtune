package com.ll.tagtune.boundedContext.comment.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Comment extends BaseEntity {
    private Boolean deleteStatus;
    private String content;
    @ColumnDefault("0")
    private int recommendCnt;
    @ManyToOne(fetch = FetchType.LAZY)
    Member member;
    @ManyToOne
    private Album album;
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;
    @OneToMany(mappedBy = "parent" , orphanRemoval = true)
    private List<Comment> children;

    public void changeDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public void addChildren(Comment children){
        this.children.add(children);
    }
}
