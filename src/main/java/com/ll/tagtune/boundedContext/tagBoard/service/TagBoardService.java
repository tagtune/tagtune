package com.ll.tagtune.boundedContext.tagBoard.service;

import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagBoard.repository.TagBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagBoardService {
    private final TagBoardRepository tagBoardRepository;

    @Transactional(readOnly = true)
    public Optional<TagBoard> findByTagBoard(String tagBoard) {
        return tagBoardRepository.findByTagBoard(tagBoard);
    }
}
