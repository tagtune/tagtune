package com.ll.tagtune.boundedContext.comment.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public RsData<Comment> saveComment(String content, Album album){
        Comment comment = Comment.builder()
                .deleteStatus(false)
                .content(content)
                .album(album)
                .build();

        commentRepository.save(comment);

        return RsData.of("S-1", "댓글이 정상적으로 등록되었습니다.",comment);
    }

    public RsData<Comment> modifyComment(Long id, String content) {
        Optional<Comment> commentById = commentRepository.findById(id);

        if (commentById.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }
        Comment comment = commentById.get();

        Comment modifyComment = comment.toBuilder()
                .content(content)
                .build();

        return RsData.of("S-1", "댓글을 성공적으로 수정했습니다.",modifyComment);
    }

    public RsData deleteComment(Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        if (commentById.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }

        commentRepository.deleteById(id);

        return RsData.of("S-1", "댓글을 성공적으로 삭제했습니다.");
    }

    public RsData showComment(Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        if (commentById.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }

        return RsData.successOf(commentById);
    }
}
