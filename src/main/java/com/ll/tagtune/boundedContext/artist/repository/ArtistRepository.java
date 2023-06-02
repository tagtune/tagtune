package com.ll.tagtune.boundedContext.artist.repository;

import com.ll.tagtune.boundedContext.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findById(Long id);

    Optional<Artist> findArtistByArtistName(String artistName);
}
