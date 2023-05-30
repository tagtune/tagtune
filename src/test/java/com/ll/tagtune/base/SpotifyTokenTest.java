package com.ll.tagtune.base;

import com.ll.tagtune.base.spotify.MusicSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpotifyTokenTest {
    @Test
    @DisplayName("Spotify Access Token")
    void t001() throws Exception {
        MusicSearch musicSearch = new MusicSearch();
        String out = musicSearch.search("IU");
    }
}
