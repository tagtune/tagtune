package com.ll.tagtune.boundedContext.track.repository;

import com.ll.tagtune.boundedContext.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByTitleAndArtist_Id(String title, Long id);

    Optional<Track> findByTitleAndArtist_ArtistName(String title, String artistName);
}
