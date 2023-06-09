package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.ApiTrackInfoResult;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.repository.TrackRepository;
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

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtist(final String title, final Long artistId) {
        return trackRepository.findByTitleAndArtist_Id(title, artistId);
    }

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtist(final String title, final String artistName) {
        return trackRepository.findByTitleAndArtist_ArtistName(title, artistName);
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
    public Track setTrackInfo(final TrackSearchDTO rawTrack) {
        final ApiTrackInfoResult.Track result = SearchEndpoint.getTrackInfo(
                rawTrack.name,
                rawTrack.artist
        ).track;

        final Artist artist = artistService.findByArtistName(result.artist.name)
                .orElseGet(() -> artistService.createArtist(result.artist.name));

        final String albumTitle = result.album != null ? result.album.title : AppConfig.getNameForNoData();

        final Album album = albumService.findByNameAndArtistId(albumTitle, artist.getId())
                .orElseGet(() -> albumService.createAlbum(albumTitle, artist));

        final Track track = createTrack(result.name, artist, album);
        track.getTags().addAll(result.toptags.tags.stream()
                .map(rawTag -> tagService.getOrCreateTag(rawTag.name))
                .map(tag -> trackTagService.connect(track, tag))
                .toList());

        trackRepository.save(track);

        return track;
    }
}
