package com.ll.tagtune.base;

import com.ll.tagtune.base.lastfm.ResultParser;
import com.ll.tagtune.boundedContext.tag.dto.TagDTO;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import com.ll.tagtune.boundedContext.track.dto.TrackSearchDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LastfmTest {
    @Test
    @DisplayName("Search Test")
    void t001() throws Exception {
        List<TrackSearchDTO> result = ResultParser.searchTracks("IU");
        assertThat(result).isNotEmpty();
        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("Get trackInfo Test")
    void t002() throws Exception {
        TrackInfoDTO result = ResultParser.getTrack("blueming", "IU");
        System.out.println(result);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Get topTags Test")
    void t003() throws Exception {
        List<TagDTO> tags = ResultParser.getTrackTags("blueming", "IU");
        tags.forEach(System.out::println);
        assertThat(tags).isNotEmpty();
    }

    @Test
    @DisplayName("Recommend From Tag Test")
    void t004() throws Exception {
        List<TrackSearchDTO> tracks = ResultParser.getTrackFromTag("k-pop");
        tracks.forEach(System.out::println);
        assertThat(tracks).isNotEmpty();
    }
}