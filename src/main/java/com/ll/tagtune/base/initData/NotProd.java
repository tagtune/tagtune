package com.ll.tagtune.base.initData;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.SearchEndpoint;
import com.ll.tagtune.base.lastfm.entity.TrackSearchDTO;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.tag.entity.Tag;
import com.ll.tagtune.boundedContext.tag.service.TagService;
import com.ll.tagtune.boundedContext.tagBoard.service.TagBoardService;
import com.ll.tagtune.boundedContext.tagComment.entity.TagComment;
import com.ll.tagtune.boundedContext.tagComment.service.TagCommentService;
import com.ll.tagtune.boundedContext.tagReply.entity.TagReply;
import com.ll.tagtune.boundedContext.tagReply.service.TagReplyService;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.IntStream;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            TrackService trackService,
            TagService tagService,
            TagBoardService tagBoardService,
            ArtistService artistService,
            AlbumService albumService,
            TagCommentService tagCommentService,
            TagReplyService tagReplyService
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
                Artist noArtist = artistService.createArtist(AppConfig.getNameForNoData());
                Album noAlbum = albumService.createAlbum(AppConfig.getNameForNoData(), noArtist);

                TrackSearchDTO[] rawTracks = {
                        SearchEndpoint.searchTrack("blueming", "IU").getTracks()
                                .stream().findFirst().orElseThrow(),
                        SearchEndpoint.searchTrack("테스형", "나훈아").getTracks()
                                .stream().findFirst().orElseThrow(),
                        SearchEndpoint.searchTrack("Believe").getTracks()
                                .stream().findFirst().orElseThrow(),
                        SearchEndpoint.searchTrack("Believer", "Imagine Dragons").getTracks()
                                .stream().findFirst().orElseThrow()
                };

                Tag[] tags = {
                        tagService.createTag("IU"),
                        tagService.createTag("IVE"),
                        tagService.createTag("나훈아"),
                        tagService.createTag("Believer"),
                        tagService.createTag("BrunoMars"),
                        tagService.createTag("예뻤어"),
                        tagService.createTag("BigBang"),
                        tagService.createTag("예술이야"),
                        tagService.createTag("아름다워"),
                        tagService.createTag("사랑하긴했었나요스쳐가는인연이었나요짧지않은우리함께했던시간들이자꾸내마음을가둬두네"),
                        tagService.createTag("AAAAA"),
                        tagService.createTag("BBBBB"),
                        tagService.createTag("CCCCC")
                };

                // for (TrackSearchDTO trackSearchDTO : rawTracks) System.out.println("[D2BUG]: " +trackSearchDTO);

                Track[] tracks = Arrays.stream(rawTracks)
                        .map(trackService::setTrackInfo)
                        .toArray(Track[]::new);

                // for (Track track : result) System.out.println("[D2BUG]: " + track);

                TagComment[] comments = {
                        tagCommentService.saveComment("안녕하십니까", tagBoardService.findById(13L).get(), members[0]).getData(),
                        tagCommentService.saveComment("이 노래 좋다 정말!", tagBoardService.findById(13L).get(), members[1]).getData()
                };

                TagReply[] replies = {
                        tagReplyService.saveReply(members[0], comments[1].getId(), "덧글입니다!").getData()
                };
            }
        };
    }
}