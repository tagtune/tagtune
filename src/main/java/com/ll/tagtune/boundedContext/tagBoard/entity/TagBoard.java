package com.ll.tagtune.boundedContext.tagBoard.entity;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class TagBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private Tag tag;
    @Builder.Default
    private Long popularity = 0L;
    private String tagBoardName;
}
