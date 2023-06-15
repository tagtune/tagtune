package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.entity.*;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchEndpoint {
    private static final String BASE_URL =
            "http://ws.audioscrobbler.com/2.0/?"
                    + "api_key=" + AppConfig.getLastfmClientId()
                    + "&format=json"
                    + "&method=";

    private static String setArtist(String artistName) {
        artistName = artistName.trim();
        return "&artist=" + artistName;
    }

    private static String setTrack(String trackName) {
        trackName = trackName.trim();
        return "&track=" + trackName;
    }

    private static String setTag(String tagName) {
        tagName = tagName.trim();
        return "&tag=" + tagName;
    }

    private static WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * lastfm API를 호출합니다
     *
     * @param httpMethod
     * @param body
     * @param url
     * @return map 수행 결과
     */
    private static <T> T getResponse(HttpMethod httpMethod, String body, String url, Class<T> responseType) {
        return createWebClient()
                .method(httpMethod)
                .uri(url)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    /**
     * 검색어로 검색하여 track 결과를 리턴합니다
     *
     * @param title
     * @param artist
     * @return 검색 결과
     */
    public static ApiTrackSearchResult searchTrack(String title, String artist) {
        String url = BASE_URL + "track.search"
                + setTrack(title)
                + (artist.equals("") ? "" : setArtist(artist));

        return getResponse(HttpMethod.GET, "", url, ApiTrackSearchResult.class);
    }

    /**
     * 검색어로 검색하여 track 결과를 리턴합니다
     *
     * @param title
     * @return 검색 결과
     */
    public static ApiTrackSearchResult searchTrack(String title) {
        return searchTrack(title, "");
    }

    /**
     * track 이름과, artist 명으로 track 의 세부정보를 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return track 세부정보
     */
    public static ApiTrackInfoResult getTrackInfo(String trackName, String artistName) {
        String url = BASE_URL + "track.getInfo"
                + setTrack(trackName)
                + setArtist(artistName);

        return getResponse(HttpMethod.GET, "", url, ApiTrackInfoResult.class);
    }

    /**
     * Track 검색 결과들의 Tag 정보를 갱신합니다
     *
     * @param searchDTOs
     * @return tracks 세부정보
     */
    public static List<TrackInfoDTO> getTrackInfos(final List<TrackSearchDTO> searchDTOs) {
        Mono<List<ApiTrackInfoResult>> result = Flux.range(0, searchDTOs.size())
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(i -> createWebClient()
                        .method(HttpMethod.GET)
                        .uri(BASE_URL + "track.getInfo"
                                + setTrack(searchDTOs.get(i).getName())
                                + setArtist(searchDTOs.get(i).getArtist())
                        )
                        .retrieve()
                        .bodyToMono(ApiTrackInfoResult.class)
                )
                .sequential()
                .collectList();

        // 결과를 반환받음
        return result.block()
                .stream()
                .map(ApiTrackInfoResult::getTrackInfoDTO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * tag 이름 으로 그 tag 가 등록된 track 목록을 인기순으로 정렬하여 리턴합니다
     *
     * @param tagName
     * @return tag 가 등록된 track 목록
     */
    public static ApiTopTrackFromTag getTracksFromTag(String tagName) {
        String url = BASE_URL + "tag.gettoptracks"
                + setTag(tagName);

        return getResponse(HttpMethod.GET, "", url, ApiTopTrackFromTag.class);
    }

    /**
     * 현재 인기 리스트를 가져옵니다.
     *
     * @return Tracks
     */
    public static ApiTopTracksFromTrending getTrendingList() {
        String url = BASE_URL + "chart.gettoptracks";

        return getResponse(HttpMethod.GET, "", url, ApiTopTracksFromTrending.class);
    }
}
