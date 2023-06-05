package com.ll.tagtune.boundedContext.playlist.repository;

import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findById(Long id);

    List<Playlist> findAllByMember_id(Long memberId);
}
