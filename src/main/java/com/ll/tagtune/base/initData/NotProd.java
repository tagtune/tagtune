package com.ll.tagtune.base.initData;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.tagBoard.entity.TagBoard;
import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
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
            TagService tagService,
            TagBoardService tagBoardService
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
                Tag[] tags = {
                        tagService.createTag("IU"),
                        tagService.createTag("IVE"),
                        tagService.createTag("나훈아"),
                        tagService.createTag("Believer"),
                        tagService.createTag("Bruno Mars"),
                        tagService.createTag("예뻤어"),
                        tagService.createTag("BigBang"),
                        tagService.createTag("예술이야"),
                        tagService.createTag("아름다워"),
                        tagService.createTag("사랑하긴 했었나요 스쳐가는 인연이었나요 짧지않은 우리 함께했던 시간들이 자꾸 내 마음을 가둬두네"),
                        tagService.createTag("AAAAA"),
                        tagService.createTag("BBBBB"),
                        tagService.createTag("CCCCC")
                };

                TagBoard[] tagBoards = {
                        tagBoardService.createTagBoard()
                };
            }
        };
    }
}