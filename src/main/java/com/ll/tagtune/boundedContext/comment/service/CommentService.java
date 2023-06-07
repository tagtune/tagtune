package com.ll.tagtune.boundedContext.comment.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.repository.AlbumRepository;
import com.ll.tagtune.boundedContext.comment.dto.CommentRequestDTO;
import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepository;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    public RsData<Comment> saveComment(String content, Album album){
        Comment comment = Comment.builder()
                .deleteStatus(false)
                .content(content)
                .children(new ArrayList<>())
                .album(album)
                .build();

        commentRepository.save(comment);

        return RsData.of("S-1", "댓글이 정상적으로 등록되었습니다.",comment);
    }

    /**
     * 대댓글 메서드
     * */
    public RsData<Comment> saveReply(Long albumId, CommentRequestDTO commentRequestDTO){
//        Optional<Member> memberById = memberRepository.findById(commentRequestDTO.getMemberId());
//        if (memberById.isEmpty()) {
//            return RsData.of("F-1", "해당되는 id의 회원이 없습니다.");
//        }

//        Optional<Album> albumById = albumRepository.findById(albumId);
//        if (albumById.isEmpty()) {
//            return RsData.of("F-2", "해당되는 id의 앨범이 없습니다.");
//        }

        Optional<Comment> commentById = commentRepository.findById(commentRequestDTO.getParentId());
        if (commentById.isEmpty()) {
            return RsData.of("F-3", "해당되는 부모 id의 댓글이 없습니다.");
        }

        Comment reply = Comment.builder()
                .deleteStatus(false)
                .content(commentRequestDTO.getContent())
                .parent(commentById.get())
                .children(new ArrayList<>())
//                .album(albumById.get())
//                .member(memberById.get())
                .build();

        commentRepository.save(reply);

        commentById.get().addChildren(reply);

        return RsData.of("S-1", "대댓글이 정상적으로 등록되었습니다.",reply);
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

    /**
     * 대댓글 있을경우 삭제 상태만 변경
     * */
    public RsData deleteComment(Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        if (commentById.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }
        Comment comment = commentById.get();

        if (comment.getChildren().size() != 0) {
            comment.changeDeleteStatus(true);
        }else {
            commentRepository.deleteById(getDeletableParentComment(comment).getId());
        }

        return RsData.of("S-1", "댓글을 성공적으로 삭제했습니다.");
    }

    /**
     * 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
     * */
    private Comment getDeletableParentComment(Comment comment) {
        Comment parent = comment.getParent();
        if(parent != null && parent.getChildren().size() == 1 && parent.getDeleteStatus())
            return getDeletableParentComment(parent);
        return comment;
    }

    public RsData showComment(Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        if (commentById.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }
        Comment comment = commentById.get();

        CommentResponseDTO commentResponseDTO = comment.getDeleteStatus() ?
                new CommentResponseDTO(comment.getId(), "삭제된 댓글입니다.", comment.getMember()) :
                new CommentResponseDTO(comment.getId(), comment.getContent(), comment.getMember());

        return RsData.successOf(commentResponseDTO);
    }
}
