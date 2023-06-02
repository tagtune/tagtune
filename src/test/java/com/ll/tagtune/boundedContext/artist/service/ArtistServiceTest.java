package com.ll.tagtune.boundedContext.artist.service;


import com.ll.tagtune.boundedContext.artist.dto.ArtistDTO;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ArtistServiceTest {
    @Autowired
    private ArtistService artistService;

    @Test
    @DisplayName("Artist Create Test")
    void t001() {
        final ArtistDTO artistDTO = ArtistDTO.builder()
                .artistName("김릘잏게")
                .build();
        assertThat(artistService.findByArtistName(artistDTO.getArtistName())).isEmpty();
        final Artist artist = artistService.createArtist(artistDTO.getArtistName());
        assertThat(artist.getArtistName()).isEqualTo(artistDTO.getArtistName());
    }
}
