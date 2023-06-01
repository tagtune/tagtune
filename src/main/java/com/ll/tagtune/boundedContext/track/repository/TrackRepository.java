package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
