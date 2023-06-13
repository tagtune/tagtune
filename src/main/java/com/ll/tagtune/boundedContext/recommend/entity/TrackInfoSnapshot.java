package com.ll.tagtune.boundedContext.recommend.entity;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.recommend.util.RecommendTypeConvertor;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class TrackInfoSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @Convert(converter = RecommendTypeConvertor.class)
    private RecommendType recommendType;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    @ToString.Exclude
    @Column(columnDefinition = "TEXT")
    private String trackInfoListJson;

    public boolean isExpired() {
        return getModifyDate() == null || getModifyDate().plusHours(1).isBefore(LocalDateTime.now());
    }
}