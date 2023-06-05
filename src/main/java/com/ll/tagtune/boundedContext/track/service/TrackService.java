package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.lastfm.ResultParser;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackSearchDTO;
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
    private final TrackRepository trackRepository;
    private final TrackRepositoryImpl trackRepositoryImpl;

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtist(String title, Long artistId) {
        return trackRepository.findByTitleAndArtist_Id(title, artistId);
    }

    @Transactional(readOnly = true)
    public Optional<Track> getTrackByTitleAndArtist(String title, String artistName) {
        return trackRepository.findByTitleAndArtist_ArtistName(title, artistName);
    }

    /**
     * API 호출 결과에 title, artist 가 제공될 때 Track 을 생성하는 메소드입니다.
     *
     * @param title
     * @param artist
     * @return Track
     */
    public Track createTrack(String title, Artist artist) {
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
    public Track createTrack(String title, Artist artist, Album album) {
        Track track = Track.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .build();

        trackRepository.save(track);

        return track;
    }

    public Track updateTrack(Track track, Artist artist, Album album) {
        Track result = track.toBuilder()
                .artist(artist)
                .album(album)
                .build();

        trackRepository.save(result);

        return result;
    }

    /**
     * 유저가 title, artist 를 알고있을 때 Track 을 리턴하는 메소드입니다.
     *
     * @param title
     * @param artistName
     * @return RsData Track
     */
    public RsData<Track> searchTrackFromApi(String title, String artistName) {
        TrackSearchDTO trackDto = ResultParser.searchTracks(title, artistName).stream().findFirst().orElse(null);

        if (trackDto == null) return RsData.of("F-1", "검색 결과가 없습니다.");

        return RsData.successOf(getOrCreateTrackByDTO(trackDto));
    }

    /**
     *  유저가 title 을 알고있을 때 Track 을 리턴하는 메소드입니다.
     * @param title
     * @return
     */
    public RsData<Track> searchTrackFromApi(String title) {
        TrackSearchDTO trackDto = ResultParser.searchTracks(title).stream().findFirst().orElse(null);

        if (trackDto == null) return RsData.of("F-1", "검색 결과가 없습니다.");

        return RsData.successOf(getOrCreateTrackByDTO(trackDto));
    }

    /**
     *  Track 세부 정보가 저장되지 않았을 때 정보를 갱신하는 메소드입니다.
     * @param track
     * @return Track with Details
     */
    public Track getTrackInfo(Track track) {
        TrackInfoDTO trackDto = ResultParser.getTrack(track.getTitle(), track.getArtist().getArtistName());

        Optional<Artist> oArtist = artistService.findByArtistName(trackDto.getArtistDTO().getArtistName());
        Artist artist = oArtist
                .orElseGet(() -> artistService.createArtist(trackDto.getArtistDTO().getArtistName()));

        Optional<Album> oAlbum = albumService.findByNameAndArtistId(trackDto.getAlbumDTO().getName(), artist.getId());
        Album album = oAlbum
                .orElseGet(() -> albumService.createAlbum(trackDto.getAlbumDTO().getName(), artist.getArtistName()));

        return updateTrack(track, artist, album);
    }

    /**
     * TrackSearch 에 적합한 Track 를 생성하거나 리턴합니다.
     * <p>
     * API 에서 받아온 Track 를 반드시 받기 위해 사용합니다.
     *
     * @param trackSearchDTO
     * @return Track
     */
    public Track getOrCreateTrackByDTO(TrackSearchDTO trackSearchDTO) {
        return getTrackByTitleAndArtist(
                trackSearchDTO.getTitle(),
                trackSearchDTO.getArtistDTO().getArtistName()
        ).orElseGet(() -> createTrack(
                trackSearchDTO.getTitle(),
                artistService.getOrCreateArtistByDTO(trackSearchDTO.getArtistDTO())
        ));
    }
}
