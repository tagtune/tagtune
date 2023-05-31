package com.ll.tagtune.boundedContext.album.repository;

import com.ll.tagtune.boundedContext.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByid(Long id);

}
