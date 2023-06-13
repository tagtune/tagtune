package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;

import java.util.Optional;

public interface TrackRepositoryCustom {
    Optional<TrackDetailDTO> getTrackDetail(final Long id);

    Optional<TrackDetailDTO> getTrackDetailWithVote(final Long id, final Long memberId);

    Optional<TrackInfoDTO> getTrackInfo(final String title, final String artistName);
}