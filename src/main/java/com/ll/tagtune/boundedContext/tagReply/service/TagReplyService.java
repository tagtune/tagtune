package com.ll.tagtune.boundedContext.tagReply.service;

import com.ll.tagtune.base.event.EventAfterDelete;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import com.ll.tagtune.boundedContext.tagComment.repository.TagCommentRepository;
import com.ll.tagtune.boundedContext.tagReply.dto.TagReplyResponseDTO;
import com.ll.tagtune.boundedContext.tagReply.entity.TagReply;
import com.ll.tagtune.boundedContext.tagReply.repository.TagReplyRepository;
import com.ll.tagtune.boundedContext.tagReply.repository.TagReplyRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagReplyService {
    private final TagReplyRepository tagReplyRepository;
    private final ApplicationEventPublisher publisher;
    private final TagCommentRepository tagCommentRepository;
    private final TagReplyRepositoryImpl tagReplyRepositoryImpl;

    /**
     * 대댓글 메서드
     */
    public RsData<TagReply> saveReply(final Member member, final Long parentId, final String content) {

        Optional<TagComment> parentComment = tagCommentRepository.findById(parentId);
        if (parentComment.isEmpty()) {
            return RsData.of("F-3", "해당되는 부모 id의 댓글이 없습니다.");
        }

        TagReply tagReply = TagReply.builder()
                .content(content)
                .member(member)
                .parent(parentComment.get())
                .build();

        tagReplyRepository.save(tagReply);

        parentComment.get().addReplies(tagReply);

        return RsData.of("S-1", "대댓글이 정상적으로 등록되었습니다.", tagReply);
    }

    private void deleteReply(TagReply tagReply) {
        tagReplyRepository.delete(tagReply);
    }

    /**
     * 대댓글도 삭제, 부모댓글도 삭제시 DB 삭제
     */
    public RsData<Void> deleteReply(final Long id, final Long memberId) {
        Optional<TagReply> oReply = tagReplyRepository.findById(id);
        if (oReply.isEmpty())
            return RsData.of("F-1", "해당하는 데이터가 없습니다.");

        if (!oReply.get().getMember().getId().equals(memberId))
            return RsData.of("F-2", "잘못된 접근입니다.");

        TagReply tagReply = oReply.get();

        TagComment comment = tagReply.getParent();
        List<TagReply> tagReplies = comment.getTagReplies();
        tagReplies.remove(tagReply);

//        reply.getParent().deleteReply(reply);

        deleteReply(tagReply);
        publisher.publishEvent(new EventAfterDelete(this, tagReply.getParent()));

        return RsData.of("S-1", "대댓글이 삭제되었습니다.");
    }

    @Transactional(readOnly = true)
    public RsData<List<TagReply>> getReply(final TagComment tagComment) {
        List<TagReply> tagReplies = tagReplyRepository.findByParent(tagComment);
        return RsData.successOf(tagReplies);
    }

    @Transactional(readOnly = true)
    public List<TagReplyResponseDTO> getReplies(TagBoard tagBoard) {
        return tagReplyRepositoryImpl.getReplies(tagBoard.getId());
    }
}