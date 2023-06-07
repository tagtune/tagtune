package com.ll.tagtune.boundedContext.comment.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.dto.CommentRequestDTO;
import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    //todo 조회, 삭제, 수정, 좋아요, 대댓글,

    @BeforeEach
    void beforeEach(){
        String content = "Hello hello";
        commentService.saveComment(content, null);
    }

    @Test
    @DisplayName("RecommendCnt, DeleteStatus 저장시 기본값 세팅 잘 되는지 test")
    void t001() throws Exception{
        List<Comment> allComment = commentRepository.findAll();

        assertThat(allComment).isNotEmpty();

        Optional<Comment> comment = allComment.stream().findFirst();
        assertThat(comment.isPresent()).isTrue(); // null 체크 추가
        assertThat(comment.get().getRecommendCnt()).isEqualTo(0);
        assertThat(comment.get().getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("댓글 삭제")
    void t002() throws Exception{
        List<Comment> allComment = commentRepository.findAll();
        Optional<Comment> comment = allComment.stream().findFirst();

        commentService.deleteComment(comment.get().getId());

        List<Comment> allComment2 = commentRepository.findAll();

        assertThat(allComment2.stream().count()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 조회 by id")
    void t003() throws Exception{
        Optional<Comment> anyComment = commentRepository.findAll().stream().findAny();

        RsData rsData = commentService.showComment(anyComment.get().getId());

        assertThat(rsData.getData()).isNotNull();
    }

    @Test
    @DisplayName("댓글 수정")
    void t004() throws Exception{
        Optional<Comment> anyComment = commentRepository.findAll().stream().findAny();

        String content = "abdbdsdfawead";
        RsData<Comment> comment = commentService.modifyComment(anyComment.get().getId(), content);

        assertThat(comment.getData().getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("부모 댓글 children에 댓글 추가")
    void t005() throws Exception{
        Optional<Comment> anyComment = commentRepository.findAll().stream().findAny();
        String content = "babybaby";

        CommentRequestDTO commentResponseDTO = new CommentRequestDTO();
        commentResponseDTO.setMemberId(null);
        commentResponseDTO.setContent(content);
        commentResponseDTO.setParentId(anyComment.get().getId());

        commentService.saveReply(null, commentResponseDTO);

        assertThat(anyComment.get().getChildren().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 삭제시 getDeleteStatus()가 true")
    void t006() throws Exception{
        Optional<Comment> parentComment = commentRepository.findAll().stream().findAny();
        String content = "babybaby";

        CommentRequestDTO commentResponseDTO = new CommentRequestDTO();
        commentResponseDTO.setMemberId(null);
        commentResponseDTO.setContent(content);
        commentResponseDTO.setParentId(parentComment.get().getId());

        commentService.saveReply(null, commentResponseDTO);

        commentService.deleteComment(parentComment.get().getId());

        assertThat(parentComment.get().getDeleteStatus()).isTrue();
    }
}