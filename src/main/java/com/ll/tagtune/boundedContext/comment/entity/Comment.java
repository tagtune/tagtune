package com.ll.tagtune.boundedContext.comment.entity;

import com.ll.tagtune.base.baseEntity.BaseEntity;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.reply.entity.Reply;
import com.ll.tagtune.boundedContext.track.entity.Track;
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
public class Comment extends BaseEntity {
    @Builder.Default
    private Boolean deleteStatus = false;
    private String content;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Track track;
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    @OrderBy("createDate desc")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ToString.Exclude
    @Builder.Default
    private List<Reply> replies = new ArrayList<>();

    public void addReplies(Reply reply){
        this.replies.add(reply);
    }

    public void deleteReply(Reply reply) {
        replies.removeIf(e -> e.equals(reply));
    }
}
