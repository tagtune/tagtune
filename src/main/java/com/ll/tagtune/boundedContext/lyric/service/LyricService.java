package com.ll.tagtune.boundedContext.lyric.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import com.ll.tagtune.boundedContext.lyric.repository.LyricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class LyricService {
    private final LyricRepository lyricRepository;

    /**
     * 가사 조회 기능
     * */
    @Transactional(readOnly = true)
    public RsData<Lyric> showLyric(Long id){
        Optional<Lyric> findLyric = lyricRepository.findById(id);

        if (findLyric.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 가사가 없습니다.");
        }

        Lyric lyric = findLyric.get();

        return RsData.of("S-1", "성공적으로 조회되었습니다!", lyric);
    }

    /**
     * 가사 저장 기능
     * */
    public RsData<Lyric> saveLyric(String content){
        if (content == null) {
            return RsData.of("F-1", "저장하려는 내용이 잘못된 값입니다.");
        }

        Lyric lyric = Lyric.builder()
                .createDate(null)
                .modifyDate(null)
                .content(content)
                .build();

        lyricRepository.save(lyric);

        return RsData.of("S-1", "성공적으로 저장되었습니다!");
    }

    /**
     * 가사 수정 기능
     * */
    public RsData<Lyric> modifyLyric(Long id, String content){
        if (content == null) {
            return RsData.of("F-1", "수정하려는 내용이 잘못된 값입니다.");
        }

        Optional<Lyric> lyric = lyricRepository.findById(id);
        if(lyric.isEmpty()){
            return RsData.of("F-2", "해당되는 id의 가사가 없습니다.");
        }

        Lyric newLyric = lyric.get().toBuilder()
                .modifyDate(LocalDateTime.now())
                .content(content)
                .build();

        return RsData.of("S-1", "성공적으로 수정되었습니다!", newLyric);
    }

    public RsData<Lyric> deleteLyric(Long id){
        Optional<Lyric> lyric = lyricRepository.findById(id);
        if(lyric.isEmpty()){
            return RsData.of("F-1", "해당되는 id의 가사가 없습니다.");
        }

        lyricRepository.deleteById(id);

        return RsData.of("S-1", "성공적으로 삭제되었습니다!");
    }
}
