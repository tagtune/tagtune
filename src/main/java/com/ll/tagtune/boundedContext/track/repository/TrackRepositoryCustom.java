package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;

import java.util.Optional;

public interface TrackRepositoryCustom {
    Optional<TrackDetailDTO> getTrackDetail(final Long id);
}
