package com.ll.tagtune.boundedContext.tagVote.service;

import com.ll.tagtune.base.event.EventAfterVoteTrackTag;
import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.tagVote.dto.TagVoteCountDTO;
import com.ll.tagtune.boundedContext.tagVote.entity.TagVote;
import com.ll.tagtune.boundedContext.tagVote.repository.TagVoteRepository;
import com.ll.tagtune.boundedContext.tagVote.repository.TagVoteRepositoryImpl;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagVoteService {
    private final TagVoteRepository tagVoteRepository;
    private final TagVoteRepositoryImpl tagVoteRepositoryImpl;
    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public Optional<TagVote> findById(final Long id) {
        return tagVoteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TagVoteCountDTO> getTagVotesCount(final Long memberId) {
        return tagVoteRepositoryImpl.getTagVotesCount(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<TagVote> findByMemberAndTrackTag(final Long memberId, final Long trackTagId) {
        return tagVoteRepository.findByMember_IdAndTrackTag_Id(memberId, trackTagId);
    }

    private TagVote create(final Boolean positive, final Member member, final TrackTag trackTag) {
        TagVote tagVote = TagVote.builder()
                .positive(positive)
                .member(member)
                .trackTag(trackTag)
                .build();

        tagVoteRepository.save(tagVote);

        publisher.publishEvent(new EventAfterVoteTrackTag(
                trackTag,
                trackTag.getPopularity() + (Boolean.TRUE.equals(positive) ? 1 : -1))
        );

        return tagVote;
    }

    private TagVote update(final Boolean positive, TagVote tagVote) {
        tagVote.setPositive(positive);

        tagVoteRepository.save(tagVote);

        publisher.publishEvent(new EventAfterVoteTrackTag(
                tagVote.getTrackTag(),
                tagVote.getTrackTag().getPopularity() + (Boolean.TRUE.equals(positive) ? 2 : -2))
        );

        return tagVote;
    }

    /**
     * Member 가 TrackTag 에 Positive 를 투표합니다.
     *
     * @param positive
     * @param member
     * @param trackTag
     * @return TagVote
     */
    public RsData<TagVote> vote(final Boolean positive, final Member member, final TrackTag trackTag) {
        return findByMemberAndTrackTag(member.getId(), trackTag.getId())
                .map(tagVote -> RsData.successOf(update(positive, tagVote)))
                .orElseGet(() -> RsData.successOf(create(positive, member, trackTag)));
    }

    public RsData<Void> cancel(final Long memberId, final Long trackTagId) {
        Optional<TagVote> oTagVote = findByMemberAndTrackTag(memberId, trackTagId);
        if (oTagVote.isEmpty()) return RsData.of("F-1", "잘못된 접근입니다.");
        publisher.publishEvent(new EventAfterVoteTrackTag(
                oTagVote.get().getTrackTag(),
                oTagVote.get().getTrackTag().getPopularity()
                        + (Boolean.TRUE.equals(oTagVote.get().getPositive()) ? -1 : 1))
        );
        tagVoteRepository.delete(oTagVote.get());

        return RsData.of("S-1", "투표를 성공적으로 취소했습니다.");
    }
}
