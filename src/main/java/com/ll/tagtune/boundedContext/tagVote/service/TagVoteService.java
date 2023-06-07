package com.ll.tagtune.boundedContext.tagVote.service;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.tagVote.entity.TagVote;
import com.ll.tagtune.boundedContext.tagVote.repository.TagVoteRepository;
import com.ll.tagtune.boundedContext.track.entity.TrackTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagVoteService {
    private final TagVoteRepository tagVoteRepository;

    @Transactional(readOnly = true)
    public Optional<TagVote> findById(final Long id) {
        return tagVoteRepository.findById(id);
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

        return tagVote;
    }

    private TagVote update(final Boolean positive, TagVote tagVote) {
        tagVote.setPositive(positive);

        tagVoteRepository.save(tagVote);

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
    public TagVote vote(final Boolean positive, final Member member, final TrackTag trackTag) {
        return findByMemberAndTrackTag(member.getId(), trackTag.getId())
                .map(tagVote -> update(positive, tagVote))
                .orElseGet(() -> create(positive, member, trackTag));
    }
}
