package com.ll.tagtune.boundedContext.lyric.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.repository.LyricRepository;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class LyricServiceTest {

    @Autowired
    private LyricService lyricService;


    /**
     *
     * 테스트 코드동시에 실행시 테스트002 004 실패,,
     * 테스트 어떻게 수정해야할까요,,
     *
     * */

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

        assertThat(lyricRsdata.getMsg()).isEqualTo("성공적으로 저장되었습니다!");
    }


    @Test
    @DisplayName("lyric modify test1")
    void t002() throws Exception {
        final String content = "sing a song~~~";

        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(2L, content);

        assertThat(lyricRsdata.getData().getContent()).isEqualTo("sing a song~~~");
    }

    @Test
    @DisplayName("lyric isBlank test2")
    void t003() throws Exception {
        final String content = "   ";

        RsData<Lyric> lyricRsdata = lyricService.modifyLyric(2L, content);

        assertThat(lyricRsdata.getMsg()).isEqualTo("수정하려는 내용이 공백입니다.");
    }

    @Test
    @DisplayName("lyric showLyric test1")
    void t004() throws Exception {

        RsData<Lyric> lyricRsData = lyricService.showLyric(1L);

        assertThat(lyricRsData.getData().getContent()).isEqualTo("0__ sing~~~~");
    }

    @Test
    @DisplayName("lyric showLyric test2")
    void t005() throws Exception {

        RsData<Lyric> lyricRsData = lyricService.showLyric(12L);

        assertThat(lyricRsData.getResultCode()).isEqualTo("F-1");
    }




}