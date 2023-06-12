package com.ll.tagtune.boundedContext.track.service;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.ApiTrackSearchResult;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackTagDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackTagStatusDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
        final String tgtTitle = "jean";
        ApiTrackSearchResult trackSearchResult = SearchEndpoint.searchTrack(tgtTitle);

        assertThat(trackSearchResult.getTracks()).isNotEmpty();
        trackSearchResult.getTracks().forEach(track -> assertThat(track.name.toLowerCase()).contains(tgtTitle));
    }

    @Test
    @DisplayName("Search Track Detail Test")
    void t002() throws Exception {
        final String tgtTitle = "좋은 날";
        final String tgtArtist = "IU";
        ApiTrackSearchResult rsTrack = SearchEndpoint.searchTrack(tgtTitle, tgtArtist);

        Track track = trackService.setTrackInfo(rsTrack.getTracks().stream().findFirst().orElseThrow());
        assertThat(track.getAlbum().getName()).isNotNull();

        List<TrackTag> tags = track.getTags();
        tags.forEach(System.out::println);
        assertThat(tags).isNotEmpty();
    }

    @Test
    @DisplayName("Track & Tag Detail DTO Test")
    void t003() throws Exception {
        final String tgtTitle = "밤편지";
        final String tgtArtist = "IU";
        final TrackSearchDTO trackSearchDTO = SearchEndpoint.searchTrack(tgtTitle, tgtArtist)
                .getTracks().stream().findFirst().orElseThrow();
        final Track track = trackService.setTrackInfo(trackSearchDTO);

        RsData<TrackDetailDTO> trackDTO = trackService.getTrackDetail(track.getId());
        assertThat(trackDTO.isSuccess()).isTrue();
        assertThat(trackDTO.getData().getArtistName()).isEqualTo(tgtArtist);
        assertThat(trackDTO.getData().getAlbumName()).isEqualTo(track.getAlbum().getName());

        List<TrackTagStatusDTO> tags = trackDTO.getData().getTags();
        assertThat(tags).isNotEmpty();
        tags.forEach(tag -> assertThat(tag.getTagName()).isNotBlank());
    }
}
