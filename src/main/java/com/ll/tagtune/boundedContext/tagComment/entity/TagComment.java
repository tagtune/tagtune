package com.ll.tagtune.boundedContext.tagComment.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagReply.entity.TagReply;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class TagComment extends BaseEntity {
    @Builder.Default
    private Boolean deleteStatus = false;
    private String content;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private TagBoard tagBoard;
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    @OrderBy("createDate desc")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ToString.Exclude
    @Builder.Default
    private List<TagReply> tagReplies = new ArrayList<>();

    public void addReplies(TagReply tagReply) {
        this.tagReplies.add(tagReply);
    }

    public void deleteReply(TagReply tagReply) {
        tagReplies.removeIf(e -> e.equals(tagReply));
    }
}
