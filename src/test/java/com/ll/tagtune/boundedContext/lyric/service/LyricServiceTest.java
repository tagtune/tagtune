package com.ll.tagtune.boundedContext.lyric.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.repository.LyricRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class LyricServiceTest {
    @Autowired
    private LyricService lyricService;

    @Autowired
    LyricRepository lyricRepository;

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 2; i++) {
            lyricService.saveLyric(("%d__ sing~~~~").formatted(i));
        }
    }

    @Test
    @DisplayName("lyric save test")
    void t001() throws Exception {
        final String content = "sing~~";

        RsData<Lyric> lyricRsdata = lyricService.saveLyric(content);

        assertThat(lyricRsdata.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("lyric modify test")
    void t002() throws Exception {
        final String content = "sing a song~~~";

        List<Lyric> lyrics = lyricRepository.findAll();
        Lyric anyLyric = lyrics.stream().findAny().get();
        LocalDateTime originCreateDate = anyLyric.getCreateDate();

        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(anyLyric.getId(), content);

        //createDate 수정 전,후가 같은지 확인
        assertThat(originCreateDate).isEqualTo(lyricRsdata.getData().getCreateDate());
        assertThat(lyricRsdata.getData().getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("lyric isNull test")
    void t003() throws Exception {

        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(2L, null);

        assertThat(lyricRsdata.isFail()).isTrue();
    }

    @Test
    @DisplayName("lyric showLyric test1")
    void t004() throws Exception {
        final String content = "0__ sing~~~~";

        List<Lyric> lyrics = lyricRepository.findAll();
        Lyric anyLyric = lyrics.stream().findAny().get();

        RsData<Lyric> lyricRsData = lyricService.showLyric(anyLyric.getId());

        assertThat(lyricRsData.getData().getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("lyric showLyric test2")
    void t005() throws Exception {

        RsData<Lyric> lyricRsData = lyricService.showLyric(64L);

        assertThat(lyricRsData.isFail()).isTrue();
    }

    @Test
    @DisplayName("lyric delete test")
    void t006() throws Exception {
        List<Lyric> lyrics = lyricRepository.findAll();
        Lyric anyLyric = lyrics.stream().findAny().get();

        RsData<Lyric> originLyricRsData = lyricService.showLyric(anyLyric.getId());

        assertThat(originLyricRsData.isSuccess()).isTrue();

        lyricService.deleteLyric(anyLyric.getId());
        RsData<Lyric> lyricRsData = lyricService.showLyric(anyLyric.getId());

        assertThat(lyricRsData.isFail()).isTrue();
    }
}