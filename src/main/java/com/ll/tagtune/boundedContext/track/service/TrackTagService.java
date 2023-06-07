package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import com.ll.tagtune.boundedContext.track.repository.TrackTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrackTagService {
    private final TrackTagRepository trackTagRepository;

    @Transactional(readOnly = true)
    public Optional<TrackTag> getTrackTag(final Long id) {
        return trackTagRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TrackTag> getTrackTags(final Track track) {
        return trackTagRepository.findAllByTrack(track);
    }

    public TrackTag connect(final Track track, final Tag tag) {
        TrackTag trackTag = TrackTag.builder()
                .track(track)
                .tag(tag)
                .build();

        trackTagRepository.save(trackTag);

        return trackTag;
    }

    public RsData<Void> delete(final Long id) {
        Optional<TrackTag> oTrackTag = trackTagRepository.findById(id);
        if (oTrackTag.isEmpty()) return RsData.of("F-1", "해당하는 데이터가 없습니다.");
        return RsData.successOf(null);
    }
}
