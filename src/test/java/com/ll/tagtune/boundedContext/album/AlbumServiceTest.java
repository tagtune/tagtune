package com.ll.tagtune.boundedContext.album;

import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
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
class AlbumServiceTest {
    @Autowired
    private AlbumService albumService;

    @Test
    @DisplayName("Album Create test")
    void t001() throws Exception {
        final String AlbumName = "cheolSoo";
        final String image = "null";

        Album album = albumService.createAlbum(AlbumName, image).getData();
        assertThat(album.getName()).isEqualTo(AlbumName);
    }
}
