package com.ll.tagtune.boundedContext.tagComment.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagComment.dto.TagCommentResponseDTO;
import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import com.ll.tagtune.boundedContext.tagComment.repository.TagCommentRepository;
import com.ll.tagtune.boundedContext.tagComment.repository.TagCommentRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagCommentService {
    private final TagCommentRepository tagCommentRepository;
    private final TagCommentRepositoryImpl tagCommentRepositoryImpl;

    public RsData<TagComment> saveComment(String content, TagBoard tagBoard, final Member member) {
        TagComment tagComment = TagComment.builder()
                .member(member)
                .content(content)
                .tagBoard(tagBoard)
                .build();

        tagCommentRepository.save(tagComment);

        return RsData.of("S-1", "댓글이 정상적으로 등록되었습니다.", tagComment);
    }

    public RsData<TagComment> modifyComment(final Long id, String content, Member member) {
        Optional<TagComment> oComment = tagCommentRepository.findById(id);

        if (oComment.isEmpty()) {
            return RsData.of("F-1", "해당되는 id의 댓글이 없습니다.");
        }

        if (!oComment.get().getMember().equals(member)) {
            return RsData.of("F-2", "다른 유저입니다.");
        }

        TagComment comment = oComment.get();

        TagComment modifyComment = comment.toBuilder()
                .content(content)
                .build();

        tagCommentRepository.save(modifyComment);

        return RsData.of("S-1", "댓글을 성공적으로 수정했습니다.", modifyComment);
    }

    /**
     * 댓글이 삭제 상태이면서, 덧글이 하나도 없을떄 DB에서 삭제합니다.
     *
     * @param tagComment
     * @return 댓글 DB 삭제 여부
     */
    private Boolean orphanDelete(TagComment tagComment) {
        if (tagComment.getDeleteStatus() && tagComment.getTagReplies().isEmpty()) {
            delete(tagComment);
            return true;
        }
        return false;
    }

    private void delete(TagComment tagComment) {
        tagCommentRepository.delete(tagComment);
    }

    public void runOrphanDelete(TagComment tagComment) {
        orphanDelete(tagComment);
    }

    /**
     * 대댓글 있을경우 삭제 상태만 변경
     */
    public RsData<Void> deleteComment(final Long id, final Long memberId) {
        Optional<TagComment> oComment = tagCommentRepository.findById(id);
        if (oComment.isEmpty())
            return RsData.of("F-1", "해당하는 데이터가 없습니다.");

        if (!oComment.get().getMember().getId().equals(memberId))
            return RsData.of("F-2", "잘못된 접근입니다.");

        TagComment tagComment = oComment.get().toBuilder().deleteStatus(true).build();
        tagCommentRepository.save(tagComment);
        orphanDelete(tagComment);

        return RsData.of("S-1", "댓글이 삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<TagCommentResponseDTO> getCommentsWithReplies(TagBoard tagBoard) {
        return tagCommentRepositoryImpl.getComments(tagBoard.getId());
    }

    public TagComment findById(Long commentId) {
        return tagCommentRepository.findById(commentId).get();
    }
}