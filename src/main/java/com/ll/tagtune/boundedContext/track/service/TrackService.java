package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.repository.TrackRepository;
import com.ll.tagtune.boundedContext.track.repository.TrackRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrackService {
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final TagService tagService;
    private final TrackRepository trackRepository;
    private final TrackTagService trackTagService;
    private final TrackRepositoryImpl trackRepositoryImpl;

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtist(final String title, final Long artistId) {
        return trackRepository.findByTitleAndArtist_Id(title, artistId);
    }

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtist(final String title, final String artistName) {
        return trackRepository.findByTitleAndArtist_ArtistName(title, artistName);
    }

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtistidAndAlbumid(final String title, final Long artistId, final Long albumId) {
        return trackRepository.findByTitleAndArtist_IdAndAlbum_Id(title, artistId, albumId);
    }

    /**
     * API 호출 결과에 title, artist 가 제공될 때 Track 을 생성하는 메소드입니다.
     *
     * @param title
     * @param artist
     * @return Track
     */
    public Track createTrack(final String title, final Artist artist) {
        Track track = Track.builder()
                .title(title)
                .artist(artist)
                .build();

        trackRepository.save(track);

        return track;
    }

    /**
     * API 호출 결과에 title, artist, album 이 제공될 때 Track 을 생성하는 메소드입니다.
     *
     * @param title
     * @param artist
     * @param album
     * @return Track
     */
    public Track createTrack(final String title, final Artist artist, final Album album) {
        Track track = Track.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .build();

        trackRepository.save(track);

        return track;
    }

    /**
     * Track 세부 정보가 저장되지 않았을 때 정보를 갱신하는 메소드입니다.
     *
     * @param rawTrack 검증되지 않은 Track
     * @return Track with Details
     */
    public Optional<Track> setTrackInfo(final TrackSearchDTO rawTrack) {
        final Optional<TrackInfoDTO> result = SearchEndpoint.getTrackInfo(
                rawTrack.name,
                rawTrack.artist
        );

        if (result.isEmpty() || result.get().getArtistName() == null) return Optional.empty();

        final Artist artist = artistService.findByArtistName(result.get().getArtistName())
                .orElseGet(() -> artistService.createArtist(result.get().getArtistName()));

        final String albumTitle = result.get().getAlbumName() != null ?
                result.get().getAlbumName() : AppConfig.getNameForNoData();

        final Album album = albumService.findByNameAndArtistId(albumTitle, artist.getId())
                .orElseGet(() -> albumService.createAlbum(albumTitle, artist));

        final Optional<Track> oTrack = getTrackByTitleAndArtistidAndAlbumid(result.get().getTitle(), artist.getId(), album.getId());
        if (oTrack.isPresent()) return oTrack;

        final Track track = createTrack(result.get().getTitle(), artist, album);

        track.getTags().addAll(result.get().getTags().stream()
                .map(tagService::getOrCreateTag)
                .map(tag -> trackTagService.connect(track, tag))
                .toList());

        trackRepository.save(track);

        return Optional.of(track);
    }

    @Transactional(readOnly = true)
    public RsData<Track> getTrack(final Long id) {
        return trackRepository.findById(id)
                .map(RsData::successOf)
                .orElseGet(() -> RsData.of("F-1", "해당하는 트랙이 없습니다."));
    }

    /**
     * Member 정보 없이 Track 세부 페이지의 정보를 가져오는 메소드입니다.
     *
     * @param id
     * @return TrackDetailDTO
     */
    @Transactional(readOnly = true)
    public RsData<TrackDetailDTO> getTrackDetail(final Long id) {
        return trackRepositoryImpl.getTrackDetail(id)
                .map(RsData::successOf)
                .orElseGet(() -> RsData.of("F-1", "해당하는 트랙이 없습니다."));
    }

    /**
     * Member TagVote 정보와 함께 Track 세부 페이지의 정보를 가져오는 메소드입니다.
     *
     * @param id
     * @param memberId
     * @return TrackDetailDTO w TagVote Status
     */
    @Transactional(readOnly = true)
    public RsData<TrackDetailDTO> getTrackDetailWithVote(final Long id, final Long memberId) {
        return trackRepositoryImpl.getTrackDetailWithVote(id, memberId)
                .map(RsData::successOf)
                .orElseGet(() -> RsData.of("F-1", "해당하는 트랙이 없습니다."));
    }

    /**
     * Track Artist, Album, Tag 정보를 가져옵니다
     *
     * @param title
     * @param artistName
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<TrackInfoDTO> getTrackInfo(final String title, final String artistName) {
        return trackRepositoryImpl.getTrackInfo(title, artistName);
    }
}
