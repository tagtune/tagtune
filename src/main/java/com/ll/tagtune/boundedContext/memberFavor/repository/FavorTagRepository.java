package com.ll.tagtune.boundedContext.memberFavor.repository;

import com.ll.tagtune.boundedContext.memberFavor.entity.FavorTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorTagRepository extends JpaRepository<FavorTag, Long> {
    List<FavorTag> findAllByMember_Id(Long memberId);
}
