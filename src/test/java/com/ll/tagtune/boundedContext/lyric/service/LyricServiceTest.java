package com.ll.tagtune.boundedContext.lyric.service;

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

import javax.security.auth.callback.LanguageCallback;
import java.time.LocalDateTime;
import java.util.List;
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

        lyricService.saveLyric( track, Language.KOREAN);
        lyricService.saveLyric( track, Language.ENGLISH);
    }

    @Test
    @DisplayName("lyric save test")
    void t001() throws Exception {
        final String content = "sing~~";

        RsData<Lyric> lyricRsdata = lyricService.saveLyric(track,Language.KOREAN);

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
        final String oriContent = "가사가 비어있습니다.";
        Optional<Lyric> oLyric = lyricService.showLyric(track.getId(), Language.KOREAN);

        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(track.getId(), content, Language.KOREAN);

        assertThat(oLyric.get().getContent()).isEqualTo(oriContent);

        //createDate 수정 전,후가 같은지 확인
        assertThat(oLyric.get().getId()).isEqualTo(lyricRsdata.getData().getId());
        assertThat(lyricRsdata.getData().getContent()).isEqualTo(content);
    }
}