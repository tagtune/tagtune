package com.ll.tagtune.boundedContext.album;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.repository.AlbumRepository;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    @DisplayName("Album Delete test3")
    void t004() throws Exception {
        long albumCountBefore = albumService.albumCount();
        assertThat(albumService.deleteAlbum(1L).isSuccess()).isTrue();
        long albumCountAfter = albumService.albumCount();
        assertThat(albumCountAfter).isEqualTo(albumCountBefore - 1);
    }

}


