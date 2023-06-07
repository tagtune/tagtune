package com.ll.tagtune.boundedContext.memberFavor.repository;

import com.ll.tagtune.boundedContext.memberFavor.entity.FavorAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorAlbumRepository extends JpaRepository<FavorAlbum, Long> {
    List<FavorAlbum> findAllByMember_Id(Long memberId);
}
