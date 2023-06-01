package com.ll.tagtune.base;

import com.ll.tagtune.base.lastfm.ResultParser;
import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.boundedContext.track.entity.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LastfmTest {
    @Test
    @DisplayName("Search Test")
    void t001() throws Exception {
        List<Track> result = ResultParser.searchTracks("IU");
        assertThat(result).isNotEmpty();
        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("Get trackInfo Test")
    void t002() throws Exception {
        Map result = SearchEndpoint.getTrackInfo("IU", "blueming");
        System.out.println(result);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Get topTags Test")
    void t003() throws Exception {
        Map result = SearchEndpoint.getTrackTopTags("IU", "blueming");
        System.out.println(result);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Recommend From Tag Test")
    void t004() throws Exception {
        Map result = SearchEndpoint.getTracksFromTag("k-pop");
        System.out.println(result);
        assertThat(result).isNotEmpty();
    }
}
