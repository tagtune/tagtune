package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.base.appConfig.AppConfig;
import com.ll.tagtune.base.lastfm.entity.*;
import com.ll.tagtune.boundedContext.track.dto.TrackInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchEndpoint {
    private static final String BASE_HOST = "ws.audioscrobbler.com";
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

    /**
     * lastfm API를 호출합니다
     *
     * @param httpMethod
     * @param body
     * @param url
     * @return map 수행 결과
     */
    private static <T> T getResponse(HttpMethod httpMethod, String body, String url, Class<T> responseType) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        // debug
        // System.out.println(url);
        httpHeaders.add("Host", BASE_HOST);
        httpHeaders.add("Content-type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<T> result = rest.exchange(
                url,
                httpMethod,
                requestEntity,
                responseType
        );

        return result.getBody();
    }

    /**
     * 검색어로 검색하여 track 결과를 리턴합니다
     *
     * @param title
     * @param artist
     * @return 검색 결과
     */
    public static ApiTrackSearchResult searchTrack(String title, String artist) {
        String body = "";
        String url = BASE_URL + "track.search"
                + setTrack(title)
                + (artist.equals("") ? "" : setArtist(artist));

        return getResponse(HttpMethod.GET, body, url, ApiTrackSearchResult.class);
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
    public static TrackInfoDTO getTrackInfo(String trackName, String artistName) {
        String body = "";
        String url = BASE_URL + "track.getInfo"
                + setTrack(trackName)
                + setArtist(artistName);

        return getResponse(HttpMethod.GET, body, url, ApiTrackInfoResult.class).getTrackInfoDTO()
                .orElseThrow(() -> new RuntimeException("해당하는 데이터가 없습니다."));
    }

    /**
     * tag 이름 으로 그 tag 가 등록된 track 목록을 인기순으로 정렬하여 리턴합니다
     *
     * @param tagName
     * @return tag 가 등록된 track 목록
     */
    public static List<TrackSearchDTO> getTracksFromTag(String tagName) {
        String body = "";
        String url = BASE_URL + "tag.gettoptracks"
                + setTag(tagName);

        return getResponse(HttpMethod.GET, body, url, ApiTopTrackFromTag.class).getTracks();
    }

    /**
     * 현재 인기 리스트를 가져옵니다.
     *
     * @return Tracks
     */
    public static List<TrackSearchDTO> getTrendingList() {
        String body = "";
        String url = BASE_URL + "chart.gettoptracks";

        return getResponse(HttpMethod.GET, body, url, ApiTopTracksFromTrending.class).getTracks();
    }
}
