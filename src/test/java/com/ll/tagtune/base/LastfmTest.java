package com.ll.tagtune.base;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.ApiTrackSearchResult;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class LastfmTest {
    @Test
    @DisplayName("Search Test")
    void t001() throws Exception {
        final String title = "spirit";
        final String artist = "Nirvana";

        ApiTrackSearchResult searchResult = SearchEndpoint.searchTrack(title, artist);
        searchResult.getTracks().forEach(t -> assertThat(t.getName().toLowerCase()).contains(title));
        // debug
        // result.getResult().forEach(System.out::println);
    }

    @Test
    @DisplayName("Get trackInfo Test")
    void t002() throws Exception {
        ApiTrackSearchResult searchResult = SearchEndpoint.searchTrack("blueming", "IU");
        assertThat(searchResult.getTracks()).isNotEmpty();

        TrackSearchDTO target = searchResult.getTracks().stream().findFirst().orElseThrow();
        TrackInfoDTO infoResult = SearchEndpoint
                .getTrackInfo(target.getName(), target.getArtist())
                .getTrackInfoDTO()
                .get();
        assertThat(infoResult).isNotNull();
        // debug
        // System.out.println(infoResult);

        assertThat(infoResult.getTitle()).isEqualTo(target.getName());
        assertThat(infoResult.getArtistName()).isEqualTo(target.getArtist());
        assertThat(infoResult.getTags()).isNotEmpty();
    }

    @Test
    @DisplayName("Recommend From Tag Test")
    void t003() throws Exception {
        List<TrackSearchDTO> searchResult = SearchEndpoint.getTracksFromTag("trot").getTracks();
        // debug
        // debug searchResult.getResult().forEach(System.out::println);
        assertThat(searchResult).isNotEmpty();
    }

    @Test
    @DisplayName("Recommend From Trending")
    void t004() throws Exception {
        List<TrackSearchDTO> searchResult = SearchEndpoint.getTrendingList().getTracks();
        // debug
        // debug searchResult.forEach(System.out::println);
        assertThat(searchResult).isNotEmpty();
        searchResult.forEach(track -> assertThat(track.getName()).isNotBlank());
    }
}