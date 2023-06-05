package com.ll.tagtune.boundedContext.tagBoard.repository;

import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagBoardRepository extends JpaRepository<TagBoard, Long> {
    Optional<TagBoard> findByTagBoardName(String tagBoardName);
}
