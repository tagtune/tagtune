package com.ll.tagtune.boundedContext.music.repository;

import com.ll.tagtune.boundedContext.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
