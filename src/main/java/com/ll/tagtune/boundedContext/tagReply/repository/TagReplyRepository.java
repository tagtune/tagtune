package com.ll.tagtune.boundedContext.tagReply.repository;

import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import com.ll.tagtune.boundedContext.tagReply.entity.TagReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagReplyRepository extends JpaRepository<TagReply, Long> {
    List<TagReply> findByParent(TagComment Tagcomment);
}
