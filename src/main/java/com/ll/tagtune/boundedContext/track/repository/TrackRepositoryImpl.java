package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackTagStatusDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.ll.tagtune.boundedContext.album.entity.QAlbum.album;
import static com.ll.tagtune.boundedContext.artist.entity.QArtist.artist;
import static com.ll.tagtune.boundedContext.tag.entity.QTag.tag;
import static com.ll.tagtune.boundedContext.tagVote.entity.QTagVote.tagVote;
import static com.ll.tagtune.boundedContext.track.entity.QTrack.track;
import static com.ll.tagtune.boundedContext.track.entity.QTrackTag.trackTag;

@RequiredArgsConstructor
public class TrackRepositoryImpl implements TrackRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private Optional<TrackDetailDTO> getTrackBase(final Long id) {
        return Optional.ofNullable(jpaQueryFactory
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
    }

    @Override
    public Optional<TrackDetailDTO> getTrackDetail(final Long id) {
        Optional<TrackDetailDTO> result = getTrackBase(id);
        result.ifPresent(trackDetailDTO -> trackDetailDTO.setTags(jpaQueryFactory
                .select(Projections.constructor(
                        TrackTagStatusDTO.class,
                        trackTag.id,
                        tag.tagName,
                        trackTag.popularity
                ))
                .from(trackTag)
                .join(trackTag.track, track).on(track.id.eq(id))
                .join(trackTag.tag, tag)
                .orderBy(trackTag.popularity.desc())
                .leftJoin(trackTag.tagVotes, tagVote)
                .fetch()));

        return result;
    }

    @Override
    public Optional<TrackDetailDTO> getTrackDetailWithVote(final Long id, final Long memberId) {
        Optional<TrackDetailDTO> result = getTrackBase(id);
        result.ifPresent(trackDetailDTO -> trackDetailDTO.setTags(jpaQueryFactory
                .select(Projections.constructor(
                        TrackTagStatusDTO.class,
                        trackTag.id,
                        tag.tagName,
                        trackTag.popularity,
                        tagVote.positive
                ))
                .from(trackTag)
                .join(trackTag.track, track).on(track.id.eq(id))
                .join(trackTag.tag, tag)
                .orderBy(trackTag.popularity.desc())
                .leftJoin(trackTag.tagVotes, tagVote)
                .fetch()));

        return result;
    }
}