package com.ll.tagtune.base.initData;

import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.service.CommentService;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.reply.entity.Reply;
import com.ll.tagtune.boundedContext.reply.service.ReplyService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            TrackService trackService,
            CommentService commentService,
            ReplyService replyService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member[] members = IntStream
                        .rangeClosed(1, 10)
                        .mapToObj(i -> memberService.join("user%d".formatted(i), "1234").getData())
                        .toArray(Member[]::new);

                memberService.join("admin", "1234");

                Track[] tracks = {
                        trackService.searchTrackFromApi("IU", "blueming").getData(),
                        trackService.searchTrackFromApi("나훈아", "테스형").getData(),
                        trackService.searchTrackFromApi("Believe").getData(),
                        trackService.searchTrackFromApi("Believer", "Imagine Dragons").getData()
                };

                Comment[] comments = {
                        commentService.saveComment("안녕하십니까", tracks[0], members[0]).getData(),
                        commentService.saveComment("이 노래 좋다 정말!", tracks[0], members[1]).getData()
                };

                Reply[] replies = {
                        replyService.saveReply(members[0], comments[1].getId(), "덧글입니다!").getData()
                };
            }
        };
    }
}