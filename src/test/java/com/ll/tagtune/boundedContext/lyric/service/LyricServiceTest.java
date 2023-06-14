package com.ll.tagtune.boundedContext.lyric.service;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.lyric.entity.Language;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.repository.LyricRepository;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class LyricServiceTest {
    @Autowired
    private LyricService lyricService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private TrackService trackService;
    @Autowired
    LyricRepository lyricRepository;
    private Track track;

    @BeforeEach
    void beforeEach() {
        Artist artist = artistService.createArtist("parkkhee");
        Album album = albumService.createAlbum("albumname", artist);

        track = trackService.createTrack("content", artist, album);

        lyricService.writeLyric(track, AppConfig.getNameForNoData(), Language.KOREAN);
        lyricService.writeLyric(track, AppConfig.getNameForNoData(), Language.ENGLISH);
    }

    @Test
    @DisplayName("lyric save test")
    void t001() throws Exception {
        RsData<Lyric> lyricRsdata = lyricService.writeLyric(track, AppConfig.getNameForNoData(), Language.KOREAN);

        assertThat(lyricRsdata.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("lyric showLyric test1")
    void t002() throws Exception {
        Optional<Lyric> oLyric = lyricService.showLyric(track.getId(), Language.KOREAN);

        assertThat(oLyric.get()).isNotNull();
    }

    @Test
    @DisplayName("lyric modify test")
    void t003() throws Exception {
        final String content = "sing a song~~~";
        Optional<Lyric> oLyric = lyricService.showLyric(track.getId(), Language.KOREAN);
        assertThat(oLyric).isPresent();

        RsData<Lyric> lyricRsdata = lyricService.writeLyric(track, content, Language.KOREAN);

        //createDate 수정 전,후가 같은지 확인
        assertThat(oLyric.get().getId()).isEqualTo(lyricRsdata.getData().getId());
        assertThat(lyricRsdata.getData().getContent()).isEqualTo(content);
    }
}