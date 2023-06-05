package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TrackServiceTest {
    @Autowired
    private TrackService trackService;

    @Test
    @DisplayName("Track Search Test")
    void t001() throws Exception {
        final String tgtTitle = "Jean";
        RsData<Track> rsTrack = trackService.searchTrackFromApi(tgtTitle);
        assertThat(rsTrack.isSuccess()).isTrue();

        assertThat(rsTrack.getData().getTitle()).contains(tgtTitle);
    }

    @Test
    @DisplayName("Search Duplicate Track Test")
    void t002() throws Exception {
        final String tgtTitle = "ice cream cake";
        RsData<Track> rsTrack = trackService.searchTrackFromApi(tgtTitle);
        assertThat(rsTrack.isSuccess()).isTrue();
        assertThat(rsTrack.getData().getTitle().toLowerCase()).contains(tgtTitle.toLowerCase());
        final Long resultId = rsTrack.getData().getId();

        assertThat(trackService.searchTrackFromApi(tgtTitle).getData().getId()).isEqualTo(resultId);
    }


    @Test
    @DisplayName("Search Track Detail Test")
    void t003() throws Exception {
        final String tgtTitle = "좋은 날";
        final String tgtArtist = "IU";
        RsData<Track> rsTrack = trackService.searchTrackFromApi(tgtTitle, tgtArtist);
        assertThat(rsTrack.isSuccess()).isTrue();

        Track track = trackService.getTrackInfo(rsTrack.getData());
        assertThat(track.getAlbum().getName()).isNotNull();

        List<TrackTag> tags = track.getTags();
        tags.forEach(System.out::println);
        assertThat(tags).isNotEmpty();
    }
}
