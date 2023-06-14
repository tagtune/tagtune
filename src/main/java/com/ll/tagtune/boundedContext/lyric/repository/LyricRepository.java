package com.ll.tagtune.boundedContext.lyric.repository;

import com.ll.tagtune.boundedContext.lyric.entity.Language;
import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LyricRepository extends JpaRepository<Lyric, Long> {
    Optional<Lyric> findById(Long id);

    Optional<Lyric> findByTrackIdAndLanguage(Long trackId, Language language);
}
