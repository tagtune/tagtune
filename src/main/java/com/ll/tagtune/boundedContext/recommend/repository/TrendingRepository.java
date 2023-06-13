package com.ll.tagtune.boundedContext.recommend.repository;

import com.ll.tagtune.boundedContext.recommend.entity.TrendingTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendingRepository extends JpaRepository<TrendingTrack, Integer> {
}
