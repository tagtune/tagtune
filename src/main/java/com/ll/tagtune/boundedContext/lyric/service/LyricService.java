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
@Transactional(readOnly = true)
@Service
public class LyricService {

    private final LyricRepository lyricRepository;



    //todo 저장, 수정, 조회

    //가사 조회
    public RsData<Lyric> showLyric(Long id){

        Optional<Lyric> findLyric = lyricRepository.findById(id);

        if (findLyric.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 가사가 없습니다.");
        }

        Lyric lyric = findLyric.get();

        return RsData.of("S-1", "성공적으로 조회되었습니다!", lyric);

    }


    //가사 저장 기능
    public RsData<Lyric> saveLyric(String content){

        if (content.isBlank() || content == null) {
            return RsData.of("F-1", "저장하려는 내용이 공백입니다.");
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
     * 기존과 내용이 같을때는 수정이 되어야하는가??
     * 수정 메서드를 이렇게 작성하는게 맞나?
     *
     * */
    //가사 수정 기능
    public RsData<Lyric> modifyLyric(Long id, String content){

        if (content.isBlank() || content == null) {
            return RsData.of("F-1", "수정하려는 내용이 공백입니다.");
        }

        Optional<Lyric> lyric = lyricRepository.findById(id);
        if(!lyric.isPresent()){
            return RsData.of("F-2", "해당되는 id의 가사가 없습니다.");
        }

        Lyric newLyric = lyric.get().toBuilder()
                .modifyDate(LocalDateTime.now())
                .content(content)
                .build();

        return RsData.of("S-1", "성공적으로 수정되었습니다!", newLyric);

    }

    //삭제 메서드도 만들어야 하나?!


}
