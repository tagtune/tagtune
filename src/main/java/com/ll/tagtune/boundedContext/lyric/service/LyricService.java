package com.ll.tagtune.boundedContext.lyric.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Language;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.repository.LyricRepository;
import com.ll.tagtune.boundedContext.track.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class LyricService {
    private final LyricRepository lyricRepository;

    /**
     * 가사 조회 기능
     */
    @Transactional(readOnly = true)
    public Optional<Lyric> showLyric(Long trackId, Language language) {
        return lyricRepository.findByTrackIdAndLanguage(trackId, language);
    }

    /**
     * 가사 저장 기능
     */
    public RsData<Lyric> saveLyric(Track track, Language language) {
        Lyric lyric = Lyric.builder()
                .track(track)
                .language(language)
                .build();

        Lyric svaelyric = lyricRepository.save(lyric);

        return RsData.of("S-1", "성공적으로 저장되었습니다!", svaelyric);
    }

    /**
     * 가사 수정 기능
     */
    public RsData<Lyric> modifyLyric(Long trackId, String content, Language language) {
        Optional<Lyric> oLyric = lyricRepository.findByTrackIdAndLanguage(trackId, language);

        if (content == null) {
            return RsData.of("F-2", "수정하려는 내용이 잘못된 값입니다.");
        }
        Lyric lyric = oLyric.get();

        Lyric newLyric = lyric.toBuilder()
                .content(content)
                .build();

        lyricRepository.save(newLyric);

        return RsData.of("S-1", "성공적으로 수정되었습니다!", newLyric);
    }

    public RsData<Lyric> deleteLyric(Long id) {
        Optional<Lyric> lyric = lyricRepository.findById(id);
        if (lyric.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 가사가 없습니다.");
        }

        lyricRepository.deleteById(id);

        return RsData.of("S-1", "성공적으로 삭제되었습니다!");
    }
}
