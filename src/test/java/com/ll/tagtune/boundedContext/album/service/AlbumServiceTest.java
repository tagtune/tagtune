package com.ll.tagtune.boundedContext.album.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.repository.AlbumRepository;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class AlbumServiceTest {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumRepository albumRepository;

    @BeforeEach
    void beforeEach() {
        Album[] albums = IntStream
                .rangeClosed(1, 10)
                .mapToObj(i -> albumService.createAlbum("Album%d".formatted(i), "1234").getData())
                .toArray(Album[]::new);
    }

//    @AfterEach
//    void afterEach() {
//        albumRepository.deleteAll();
//    }

    @Test
    @DisplayName("Album Create test")
    void t001() throws Exception {
        final String AlbumName = "cheolSoo";
        final String image = "null";

        Album album = albumService.createAlbum(AlbumName, image).getData();
        assertThat(album.getName()).isEqualTo(AlbumName);
    }

    @Test
    @DisplayName("Album Delete test")
    void t002() throws Exception {
        Long albumId = albumService.createAlbum("AlbumName", "image").getData().getId();
        RsData<Album> rsData = albumService.deleteAlbum(albumId);
        assertThat(rsData.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("Album Delete test2")
    void t003() throws Exception {
        RsData<Album> rsData = albumService.deleteAlbum(100L);
        assertThat(rsData.isFail()).isTrue();
    }

    @Test
    @DisplayName("find All Album By Name")
    void t004() throws Exception {
        final String tgtAlbum = "Album1";
        List<Album> albums = albumService.findAllByName(tgtAlbum);
        assertThat(albums).isNotEmpty();
        for (Album album : albums) {
            assertThat(album.getName()).isEqualTo(tgtAlbum);
        }
    }

    @Test
    @DisplayName("find Album By Id")
    void t005() throws Exception {
        final Long targetId = 1L;
        Album anyAlbum = albumRepository.findAll().stream().findFirst().orElseThrow();
        assertThat(albumService.findById(anyAlbum.getId()).get().getName()).isEqualTo(anyAlbum.getName());
    }
}
