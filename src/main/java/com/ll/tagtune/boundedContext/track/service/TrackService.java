package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.lastfm.ResultParser;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
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

    public Track createTrack(String title, Artist artist) {
        Track track = Track.builder()
                .title(title)
                .artist(artist)
                .build();

        trackRepository.save(track);

        return track;
    }

    public Track createTrack(String title, Artist artist, Album album) {
        Track track = Track.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .build();

        trackRepository.save(track);

        return track;
    }

    public RsData<Track> searchTrackFromApi(String title, String artistName) {
        TrackSearchDTO trackDto = ResultParser.searchTracks(title, artistName).stream().findFirst().orElse(null);

        if (trackDto == null) return RsData.of("F-1", "검색 결과가 없습니다.");

        return RsData.successOf(getOrCreateTrackByDTO(trackDto));
    }

    public RsData<Track> searchTrackFromApi(String title) {
        TrackSearchDTO trackDto = ResultParser.searchTracks(title).stream().findFirst().orElse(null);

        if (trackDto == null) return RsData.of("F-1", "검색 결과가 없습니다.");

        return RsData.successOf(getOrCreateTrackByDTO(trackDto));
    }

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
