package com.ll.tagtune.boundedContext.tag.repository;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
}
