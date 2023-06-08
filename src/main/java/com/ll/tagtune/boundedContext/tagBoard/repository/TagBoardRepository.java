package com.ll.tagtune.boundedContext.tagBoard.repository;

import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagBoardRepository extends JpaRepository<TagBoard, Long> {
    List<TagBoard> findAll();

    Optional<TagBoard> findById(Long id);

    List<TagBoard> findTop3ByPopularity(Long popularity);

    List<TagBoard> findByTagBoardNameLike(String kw);
}
