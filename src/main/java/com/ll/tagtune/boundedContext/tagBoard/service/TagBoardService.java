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

    public List<TagBoard> findTop3ByOrderByPopularityDesc() {
        return tagBoardRepository.findTop3ByOrderByPopularityDesc();
    }

    public List<TagBoard> findByTagBoardNameLike(String kw) {
        return tagBoardRepository.findByTagBoardNameLike(kw);
    }

    public Optional<TagBoard> findById(Long id) {
        return tagBoardRepository.findById(id);
    }

    /**
     * 태그가 생겼을 경우
     * tagList를 받아와서 tagBoard로 생성
     * findTop3ByOrderByPopularityDesc 체크하려고 .popularity(tag.getId()) 추가
     */
    public TagBoard createTagBoard() {
        TagBoard tagBoard = null;
        List<Tag> tagList = tagRepository.findAll();
        for (Tag tag : tagList) {
            tagBoard = TagBoard.builder()
                    .tag(tag)
                    .tagBoardName(tag.getTagName())
                    // todo 아래 코드 지워야 함
                    .popularity(tag.getId())
                    .build();
            tagBoardRepository.save(tagBoard);
        }
        return tagBoard;
    }
}
