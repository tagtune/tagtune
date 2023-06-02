package com.ll.tagtune.boundedContext.artist.service;

import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Artist> findByArtistName(final String artistName) {
        return artistRepository.findArtistByArtistName(artistName);
    }

    public Artist create(final String artistName) {
        Artist artist = Artist.builder()
                .artistName(artistName)
                .build();
        artistRepository.save(artist);

        return artist;
    }

    /**
     * artistName 에 해당하는 Artist 를 생성하거나 리턴합니다.
     * <p>
     * API 에서 받아온 Artist 를 반드시 받기 위해 사용합니다.
     *
     * @param artistDTO
     * @return Artist
     */
    public Artist getOrCreateArtistByName(ArtistDTO artistDTO) {
        Optional<Artist> oArtist = findByArtistName(artistDTO.getArtistName());
        return oArtist.orElseGet(() -> create(artistDTO.getArtistName()));
    }
}
