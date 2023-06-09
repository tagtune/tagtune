package com.ll.tagtune.base;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.ApiTopTrackFromTag;
import com.ll.tagtune.base.lastfm.entity.ApiTrackInfoResult;
import com.ll.tagtune.base.lastfm.entity.ApiTrackSearchResult;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LastfmTest {
    @Test
    @DisplayName("Search Test")
    void t001() throws Exception {
        final String title = "spirit";
        final String artist = "Nirvana";

        ApiTrackSearchResult searchResult = SearchEndpoint.searchTrack(title, artist);
        searchResult.getTracks().forEach(t -> assertThat(t.name.toLowerCase()).contains(title));
        // debug
        // result.getResult().forEach(System.out::println);
    }

    @Test
    @DisplayName("Get trackInfo Test")
    void t002() throws Exception {
        ApiTrackSearchResult searchResult = SearchEndpoint.searchTrack("blueming", "IU");
        assertThat(searchResult.getTracks()).isNotEmpty();

        TrackSearchDTO target = searchResult.getTracks().stream().findFirst().orElseThrow();
        ApiTrackInfoResult infoResult = SearchEndpoint.getTrackInfo(target.name, target.artist);
        assertThat(infoResult).isNotNull();
        // debug
        // System.out.println(infoResult);

        assertThat(infoResult.track.name).isEqualTo(target.name);
        assertThat(infoResult.track.artist.name).isEqualTo(target.artist);
        assertThat(infoResult.track.toptags.tags).isNotEmpty();
    }

    @Test
    @DisplayName("Recommend From Tag Test")
    void t003() throws Exception {
        ApiTopTrackFromTag searchResult = SearchEndpoint.getTracksFromTag("trot");
        // debug
        // debug searchResult.getResult().forEach(System.out::println);
        assertThat(searchResult.getTracks()).isNotEmpty();
    }
}