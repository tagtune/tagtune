package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackTagDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.ll.tagtune.boundedContext.album.entity.QAlbum.album;
import static com.ll.tagtune.boundedContext.artist.entity.QArtist.artist;
import static com.ll.tagtune.boundedContext.tag.entity.QTag.tag;
import static com.ll.tagtune.boundedContext.track.entity.QTrack.track;
import static com.ll.tagtune.boundedContext.track.entity.QTrackTag.trackTag;

@RequiredArgsConstructor
public class TrackRepositoryImpl implements TrackRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TrackDetailDTO> getTrackDetail(final Long id) {
        Optional<TrackDetailDTO> result = Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(
                        TrackDetailDTO.class,
                        track.id,
                        track.title,
                        track.artist.id,
                        track.artist.artistName,
                        track.album.id,
                        track.album.name
                ))
                .from(track)
                .leftJoin(track.artist, artist)
                .leftJoin(track.album, album)
                .where(track.id.eq(id))
                .fetchOne()
        );
        result.ifPresent(trackDetailDTO -> trackDetailDTO.setTags(jpaQueryFactory
                .select(Projections.constructor(
                        TrackTagDTO.class,
                        trackTag.id,
                        tag.tagName,
                        trackTag.voteCount
                ))
                .from(trackTag)
                .join(trackTag.track, track).on(track.id.eq(id))
                .join(trackTag.tag, tag)
                .fetch()));

        return result;
    }
}