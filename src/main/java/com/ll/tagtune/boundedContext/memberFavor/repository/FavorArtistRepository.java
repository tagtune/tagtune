package com.ll.tagtune.boundedContext.memberFavor.repository;

import com.ll.tagtune.boundedContext.memberFavor.entity.FavorArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorArtistRepository extends JpaRepository<FavorArtist, Long> {
    List<FavorArtist> findAllByMember_Id(Long memberId);
}
