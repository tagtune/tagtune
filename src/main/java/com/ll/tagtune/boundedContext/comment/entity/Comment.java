package com.ll.tagtune.boundedContext.comment.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.album.entity.Album;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;

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
    //    Member member;  //todo 유저와의 연관관계
    @ManyToOne
    private Album album;
}
