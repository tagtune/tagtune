package com.ll.tagtune.boundedContext.tagBoard.service;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
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

    @Transactional(readOnly = true)
    public List<TagBoard> findAll() {
        return tagBoardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<TagBoard> findTop3ByOrderByPopularityDesc() {
        return tagBoardRepository.findTop3ByOrderByPopularityDesc();
    }

    @Transactional(readOnly = true)
    public List<TagBoard> findByTagBoardNameLike(String kw) {
        return tagBoardRepository.findByTagBoardNameLike(kw);
    }

    @Transactional(readOnly = true)
    public Optional<TagBoard> findById(Long id) {
        return tagBoardRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<TagBoard> findByTagId(final Long tagId) {
        return tagBoardRepository.findByTagId(tagId);
    }

    public TagBoard create(Tag tag) {
        TagBoard tagBoard = TagBoard.builder()
                .tag(tag)
                .tagBoardName(tag.getTagName())
                .build();

        tagBoardRepository.save(tagBoard);

        return tagBoard;
    }

    public void updatePopularity(TagBoard tagBoard) {
        TagBoard modifyTagBoard = tagBoard.toBuilder()
                .popularity(tagBoard.getPopularity() + 1)
                .build();

        tagBoardRepository.save(modifyTagBoard);
    }
}
