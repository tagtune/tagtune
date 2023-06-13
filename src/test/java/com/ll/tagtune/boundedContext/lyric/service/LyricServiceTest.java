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

        for (int i = 0; i < 2; i++) {
            lyricService.saveLyric(("%d__ sing~~~~").formatted(i), track, Language.KOREAN);
        }
    }

    @Test
    @DisplayName("lyric save test")
    void t001() throws Exception {
        final String content = "sing~~";

        RsData<Lyric> lyricRsdata = lyricService.saveLyric(content,track,Language.KOREAN);

        assertThat(lyricRsdata.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("lyric showLyric test1")
    void t002() throws Exception {
        Optional<Lyric> oLyricsList = lyricService.showLyric(track.getId(), Language.KOREAN);

        assertThat(oLyricsList.get()).isNotNull();
    }

//    @Test
//    @DisplayName("lyric modify test")
//    void t002() throws Exception {
//        final String content = "sing a song~~~";
//
//        List<Lyric> lyrics = lyricRepository.findAll();
//        Lyric anyLyric = lyrics.stream().findAny().get();
//        LocalDateTime originCreateDate = anyLyric.getCreateDate();
//
//        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(anyLyric.getId(), content);
//
//        //createDate 수정 전,후가 같은지 확인
//        assertThat(originCreateDate).isEqualTo(lyricRsdata.getData().getCreateDate());
//        assertThat(lyricRsdata.getData().getContent()).isEqualTo(content);
//    }

//    @Test
//    @DisplayName("lyric isNull test")
//    void t003() throws Exception {
//
//        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(2L, null);
//
//        assertThat(lyricRsdata.isFail()).isTrue();
//    }

//    @Test
//    @DisplayName("lyric delete test")
//    void t006() throws Exception {
//        List<Lyric> lyrics = lyricRepository.findAll();
//        Lyric anyLyric = lyrics.stream().findAny().get();
//
//        Optional<List<Lyric>> oLyricList = lyricService.showLyric(track);
//
//        assertThat(oLyricList.isSuccess()).isTrue();
//
//        lyricService.deleteLyric(anyLyric.getId());
//        RsData<Lyric> lyricRsData = lyricService.showLyric(anyLyric.getId());
//
//        assertThat(lyricRsData.isFail()).isTrue();
//    }
}