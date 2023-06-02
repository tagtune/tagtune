package com.ll.tagtune.boundedContext.artist.service;

import com.ll.tagtune.base.rsData.RsData;
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
    public Optional<Artist> findById(final Long id) {
        return artistRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Artist> findByArtistName(final String artistName) {
        return artistRepository.findArtistByArtistName(artistName);
    }

    /**
     * 유저가 아티스트를 접근할 때 사용하는 메소드입니다.
     *
     * @param id
     * @return RsData<Artist>
     */
    @Transactional(readOnly = true)
    public RsData<Artist> getArtist(final Long id) {
        return findById(id)
                .map(RsData::successOf)
                .orElseGet(() -> RsData.of("F-1", "해당 아티스트는 없습니다."));
    }

    public Artist createArtist(final String artistName) {
        Artist artist = Artist.builder()
                .artistName(artistName)
                .build();

        return artistRepository.save(artist);
    }

    /**
     * ArtistDTO 에 적합한 Artist 를 생성하거나 리턴합니다.
     * <p>
     * API 에서 받아온 Artist 를 반드시 받기 위해 사용합니다.
     *
     * @param artistDTO
     * @return Artist
     */
    public Artist getOrCreateArtistByDTO(ArtistDTO artistDTO) {
        Optional<Artist> oArtist = findByArtistName(artistDTO.getArtistName());
        return oArtist.orElseGet(() -> createArtist(artistDTO.getArtistName()));
    }
}
