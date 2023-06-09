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
            ArtistService artistService,
            AlbumService albumService,
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
                // for (TrackSearchDTO trackSearchDTO : rawTracks) System.out.println("[D2BUG]: " +trackSearchDTO);

                Track[] tracks = Arrays.stream(rawTracks)
                        .map(trackService::setTrackInfo)
                        .toArray(Track[]::new);

                // for (Track track : tracks) System.out.println("[D2BUG]: " + track);
            }
        };
    }
}