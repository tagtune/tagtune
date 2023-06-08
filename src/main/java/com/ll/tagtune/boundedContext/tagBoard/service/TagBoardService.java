package com.ll.tagtune.boundedContext.tagBoard.service;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.repository.TagRepository;
import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagBoard.repository.TagBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagBoardService {
    private final TagBoardRepository tagBoardRepository;
    private final TagRepository tagRepository;

    public List<TagBoard> findAll() {
        return tagBoardRepository.findAll();
    }

    public List<TagBoard> findTop3ByPopularity(Long popularity) {
        return tagBoardRepository.findTop3ByPopularity(popularity);
    }

    public List<TagBoard> findByTagBoardNameLike(String kw) {
        return tagBoardRepository.findByTagBoardNameLike(kw);
    }

    public Optional<TagBoard> findById(Long id) {
        return tagBoardRepository.findById(id);
    }

    public TagBoard createTagBoard() {
        TagBoard tagBoard = null;
        List<Tag> tagList = tagRepository.findAll();
        for (Tag tag : tagList) {
            tagBoard = TagBoard.builder()
                    .tag(tag)
                    .tagBoardName(tag.getTagName())
                    .build();
            tagBoardRepository.save(tagBoard);
        }
        return tagBoard;
    }
}
