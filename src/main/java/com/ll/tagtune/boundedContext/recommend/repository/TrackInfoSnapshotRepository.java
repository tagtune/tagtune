package com.ll.tagtune.boundedContext.recommend.repository;

import com.ll.tagtune.boundedContext.recommend.entity.RecommendType;
import com.ll.tagtune.boundedContext.recommend.entity.TrackInfoSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackInfoSnapshotRepository extends JpaRepository<TrackInfoSnapshot, Integer> {
    Optional<TrackInfoSnapshot> findByMember_IdAndRecommendType(
            final Long memberId,
            final RecommendType recommendType
    );
}
