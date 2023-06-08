package com.ll.tagtune.boundedContext.comment.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepository;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepositoryImpl;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.track.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentRepositoryImpl commentRepositoryImpl;

    public RsData<Comment> saveComment(String content, Track track, final Member member){
        Comment comment = Comment.builder()
                .member(member)
                .content(content)
                .track(track)
                .build();

        commentRepository.save(comment);

        return RsData.of("S-1", "댓글이 정상적으로 등록되었습니다.",comment);
    }

    public RsData<Comment> modifyComment(final Long id, String content) {
        Optional<Comment> commentById = commentRepository.findById(id);

        if (commentById.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }
        Comment comment = commentById.get();

        Comment modifyComment = comment.toBuilder()
                .content(content)
                .build();

        commentRepository.save(modifyComment);

        return RsData.of("S-1", "댓글을 성공적으로 수정했습니다.",modifyComment);
    }

    /**
     * 댓글이 삭제 상태이면서, 덧글이 하나도 없을떄 DB에서 삭제합니다.
     * @param comment
     * @return  댓글 DB 삭제 여부
     */
    private Boolean orphanDelete(Comment comment) {
        if(comment.getDeleteStatus() && comment.getReplies().isEmpty()) {
            delete(comment);
            return true;
        }
        return false;
    }

    private void delete(Comment comment){
        commentRepository.delete(comment);
    }

    public void runOrphanDelete(Comment comment) {
        orphanDelete(comment);
    }

    /**
     * 대댓글 있을경우 삭제 상태만 변경
     * */
    public RsData<Void> deleteComment(final Long id, final Long memberId) {
        Optional<Comment> oComment = commentRepository.findById(id);
        if (oComment.isEmpty())
            return RsData.of("F-1", "해당하는 데이터가 없습니다.");

        if (!oComment.get().getMember().getId().equals(memberId))
            return RsData.of("F-2", "잘못된 접근입니다.");

        Comment comment = oComment.get().toBuilder().deleteStatus(true).build();
        commentRepository.save(comment);
        orphanDelete(comment);

        return RsData.of("S-1", "댓글이 삭제되었습니다.");
    }

    public List<CommentResponseDTO> getCommentsWithReplies(Track track) {
        return commentRepositoryImpl.getComments(track.getId());
    }
}
