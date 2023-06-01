package com.ll.tagtune.base.initData;

import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
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
            TrackService trackService
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
            }
        };
    }
}