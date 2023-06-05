package com.ll.tagtune.boundedContext.tagComment.service;

import com.ll.tagtune.boundedContext.tagComment.repository.TagCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagCommentService {
    private final TagCommentRepository tagCommentRepository;
}
