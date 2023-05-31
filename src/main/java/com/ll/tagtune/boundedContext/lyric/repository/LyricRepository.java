package com.ll.tagtune.boundedContext.lyric.repository;

import com.ll.tagtune.boundedContext.lyric.entity.Lyric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LyricRepository extends JpaRepository<Lyric, Long> {

    Optional<Lyric> findById(Long id);


}
