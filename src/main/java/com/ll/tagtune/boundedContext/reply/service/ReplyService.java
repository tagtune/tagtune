package com.ll.tagtune.boundedContext.reply.service;

import com.ll.tagtune.base.event.EventAfterReplyDelete;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepository;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.reply.dto.ReplyDTO;
import com.ll.tagtune.boundedContext.reply.entity.Reply;
import com.ll.tagtune.boundedContext.reply.repository.ReplyRepository;
import com.ll.tagtune.boundedContext.reply.repository.ReplyRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final ApplicationEventPublisher publisher;
    private final CommentRepository commentRepository;
    private final ReplyRepositoryImpl replyRepositoryIml;

    /**
     * 대댓글 메서드
     */
    public RsData<Reply> saveReply(final Member member, final Long parentId, final String content) {
        Optional<Comment> parentComment = commentRepository.findById(parentId);
        if (parentComment.isEmpty()) {
            return RsData.of("F-3", "해당되는 부모 id의 댓글이 없습니다.");
        }

        Reply reply = Reply.builder()
                .content(content)
                .member(member)
                .parent(parentComment.get())
                .build();

        replyRepository.save(reply);

        parentComment.get().addReplies(reply);

        return RsData.of("S-1", "대댓글이 정상적으로 등록되었습니다.", reply);
    }

    public RsData<Reply> modifyReply(
            final Long replyId,
            final Long parentCommentId,
            final String content,
            final Long memberId
    ) {
        Optional<Comment> parentComment = commentRepository.findById(parentCommentId);
        if (parentComment.isEmpty()) {
            return RsData.of("F-1", "댓글에 대한 잘못된 접근입니다.");
        }

        Optional<Reply> oReply = replyRepository.findById(replyId);
        if (!oReply.get().getMember().getId().equals(memberId))
            return RsData.of("F-2", "잘못된 접근입니다.");
        if (oReply.isEmpty()) {
            return RsData.of("F-3", "대댓글에 대한 잘못된 접근입니다.");
        }

        Reply reply = oReply.get();

        Comment comment = reply.getParent();
        List<Reply> replies = comment.getReplies();
        replies.remove(reply);

        //전에 있던 reply를 지우기
        Reply newReply = oReply.get().toBuilder()
                .content(content)
                .build();

        replyRepository.save(newReply);

        parentComment.get().addReplies(newReply); //새로 수정된 reply를 추가한다.

        return RsData.of("S-1", "대댓글이 정상적으로 등록되었습니다.", newReply);
    }

    private void deleteReply(Reply reply) {
        replyRepository.delete(reply);
    }

    /**
     * 대댓글도 삭제, 부모댓글도 삭제시 DB 삭제
     */
    public RsData<Void> deleteReply(final Long id, final Long memberId) {
        Optional<Reply> oReply = replyRepository.findById(id);
        if (oReply.isEmpty())
            return RsData.of("F-1", "해당하는 데이터가 없습니다.");
        if (!oReply.get().getMember().getId().equals(memberId))
            return RsData.of("F-2", "잘못된 접근입니다.");

        Reply reply = oReply.get();

        Comment comment = reply.getParent();
        List<Reply> replies = comment.getReplies();
        replies.remove(reply);

        deleteReply(reply);
        publisher.publishEvent(new EventAfterReplyDelete(reply.getParent()));

        return RsData.of("S-1", "대댓글이 삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public RsData<List<ReplyDTO>> getReplies(final Long commentId) {
        List<ReplyDTO> replies = replyRepositoryIml.getReplies(commentId);
        return RsData.successOf(replies);
    }
}
