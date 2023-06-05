package com.ll.tagtune.boundedContext.tag.service;

import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.repository.TagRepository;
import com.ll.tagtune.boundedContext.tag.repository.TagRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagRepositoryImpl tagRepositoryImpl;

    @Transactional(readOnly = true)
    public Optional<Tag> findById(final Long id) {
        return tagRepository.findById(id);
    }

    /**
     * Tag 를 name 으로 찾습니다.
     * 중복 관련 로직이 필요할 수 있습니다.
     *
     * @param name
     * @return Tag
     */
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(final String name) {
        return tagRepository.findByTagName(name);
    }

    /**
     *  페이징이 적용된 Tag 목록을 리턴합니다.
     *
     * @param page
     * @return Page<Tag>
     */
    @Transactional(readOnly = true)
    public Page<Tag> getTagList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.tagRepository.findAll(pageable);
    }


    /**
     * Tag 의 대소문자를 무시하면 같은 태그인지 비교합니다.
     *
     * @param src
     * @param tgt
     * @return Tag 의 중복 여부
     */
    @Transactional(readOnly = true)
    public Boolean isDuplicate(Tag src, Tag tgt) {
        return src.getTagName().equalsIgnoreCase(tgt.getTagName());
    }

    public Tag createTag(final String name) {
        Tag tag = Tag.builder()
                .tagName(name)
                .build();

        tagRepository.save(tag);

        return tag;
    }

    public Tag updateTag(Tag tag, final String name) {
        Tag result = tag.toBuilder()
                .tagName(name)
                .build();

        tagRepository.save(tag);

        return result;
    }
}
