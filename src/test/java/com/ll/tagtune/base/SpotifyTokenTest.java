package com.ll.tagtune.base;

import com.ll.tagtune.base.spotify.CreateToken;
import com.ll.tagtune.base.spotify.MusicSearch;
import com.ll.tagtune.boundedContext.music.entity.Music;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpotifyTokenTest {
    @Test
    @DisplayName("Spotify Access Token")
    void t001() throws Exception {
        final String accessToken = CreateToken.getAccessToken();
        System.out.println(accessToken);
        assertThat(accessToken).isNotBlank();
    }

    @Test
    @DisplayName("Spotify Search Parse")
    void t002() throws Exception {
        MusicSearch musicSearch = new MusicSearch();
        List<Music> out = musicSearch.getSearchResult("IU");
        System.out.println(out);
        assertThat(out).isNotEmpty();
    }
}
