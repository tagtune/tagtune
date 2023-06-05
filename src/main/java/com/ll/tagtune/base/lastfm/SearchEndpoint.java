package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.base.appConfig.AppConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
    private static Map getResponse(HttpMethod httpMethod, String body, String url) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        System.out.println(url);
        httpHeaders.add("Host", BASE_HOST);
        httpHeaders.add("Content-type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<Map> responseEntity = rest
                .exchange(url, httpMethod, requestEntity, Map.class);


        return responseEntity.getBody();
    }

    /**
     * 검색어로 검색하여 track 결과를 리턴합니다
     *
     * @param query 검색어
     * @return 검색 결과
     */
    public static Map searchTrack(String query) {
        String body = "";
        String url = BASE_URL + "track.search"
                + setTrack(query);

        return getResponse(HttpMethod.GET, body, url);
    }

    /**
     * 검색어로 검색하여 track 결과를 리턴합니다
     *
     * @param title
     * @param artist
     * @return 검색 결과
     */
    public static Map searchTrack(String title, String artist) {
        String body = "";
        String url = BASE_URL + "track.search"
                + setTrack(title)
                + setTrack(artist);


        return getResponse(HttpMethod.GET, body, url);
    }

    /**
     * track 이름과, artist 명으로 track 의 세부정보를 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return track 세부정보
     */
    public static Map getTrackInfo(String trackName, String artistName) {
        String body = "";
        String url = BASE_URL + "track.getInfo"
                + setArtist(artistName)
                + setTrack(trackName);

        return getResponse(HttpMethod.GET, body, url);
    }

    /**
     * track 이름과, artist 명으로 track 에 등록된 tag 를 많은 순으로 정렬하여 리턴합니다
     *
     * @param trackName
     * @param artistName
     * @return tag 목록
     */
    public static Map getTrackTopTags(String trackName, String artistName) {
        String body = "";
        String url = BASE_URL + "track.gettoptags"
                + setArtist(artistName)
                + setTrack(trackName);

        return getResponse(HttpMethod.GET, body, url);
    }

    /**
     * tag 이름 으로 그 tag 가 등록된 track 목록을 인기순으로 정렬하여 리턴합니다
     *
     * @param tagName
     * @return tag 가 등록된 track 목록
     */
    public static Map getTracksFromTag(String tagName) {
        String body = "";
        String url = BASE_URL + "tag.gettoptracks"
                + setTag(tagName);

        return getResponse(HttpMethod.GET, body, url);
    }
}
