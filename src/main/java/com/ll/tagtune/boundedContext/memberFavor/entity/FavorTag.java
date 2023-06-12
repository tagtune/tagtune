package com.ll.tagtune.boundedContext.memberFavor.entity;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString
public class FavorTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Tag tag;
}
