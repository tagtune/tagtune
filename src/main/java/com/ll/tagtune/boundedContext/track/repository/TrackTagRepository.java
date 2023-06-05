package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackTagRepository extends JpaRepository<TrackTag, Long> {
    List<TrackTag> findAllByTrack(Track track);
}
