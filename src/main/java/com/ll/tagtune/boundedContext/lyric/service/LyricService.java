package com.ll.tagtune.boundedContext.lyric.service;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.dto.LyricDTO;
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
     * 가사를 DTO로 리턴합니다
     *
     * @param trackId
     * @param language
     * @return LyricDTO
     */
    @Transactional(readOnly = true)
    public LyricDTO showLyricDTO(final Long trackId, final Language language) {
        return showLyric(trackId, language)
                .map(l -> LyricDTO.builder()
                        .language(l.getLanguage().getName())
                        .content(l.getContent())
                        .title(l.getTrack().getTitle())
                        .build())
                .orElseGet(() -> LyricDTO.builder()
                        .language(language.getName())
                        .content(" ")
                        .title(AppConfig.getNameForNoData())
                        .build());
    }

    /**
     * 가사 조회 기능
     */
    @Transactional(readOnly = true)
    public Optional<Lyric> showLyric(final Long trackId, final Language language) {
        return lyricRepository.findByTrackIdAndLanguage(trackId, language);
    }

    /**
     * 가사 작성 기능
     */
    public RsData<Lyric> writeLyric(final Track track, final String content, final Language language) {
        if (content == null) return RsData.of("F-2", "수정하려는 내용이 잘못된 값입니다.");

        return lyricRepository.findByTrackIdAndLanguage(track.getId(), language)
                .map(lyric -> RsData.of("S-2", "성공적으로 작성했습니다!", modifyLyric(lyric, content)))
                .orElseGet(() -> RsData.of("S-1", "성공적으로 작성했습니다!", createLyric(track, content, language)));
    }

    private Lyric createLyric(final Track track, final String content, final Language language) {
        Lyric lyric = Lyric.builder()
                .track(track)
                .content(content)
                .language(language)
                .build();

        lyricRepository.save(lyric);

        return lyric;
    }

    /**
     * 가사 수정 기능
     */
    private Lyric modifyLyric(Lyric lyric, final String content) {
        lyric = lyric.toBuilder()
                .content(content)
                .build();

        lyricRepository.save(lyric);

        return lyric;
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
