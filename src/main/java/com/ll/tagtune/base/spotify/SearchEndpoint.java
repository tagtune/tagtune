package com.ll.tagtune.base.spotify;

import com.ll.tagtune.base.api.RequestTemplate;
import com.ll.tagtune.boundedContext.music.entity.Music;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SearchEndpoint {
    private static final String DEFAULT_MARKET = "KR";
    private static final SpotifyType DEFAULT_TYPE = SpotifyType.TRACK;
    private static final int DEFAULT_LIMIT = 5;

    /**
     * 스포티파이 검색 엔드포인트를 호출합니다
     *
     * @param query 검색할 문자열
     * @return 검색 결과 Json String
     */
    private Map search(String query) {
        final String accessToken = CreateToken.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");

        String body = "";
        String url = "https://api.spotify.com/v1/search?&q=" + query
                + "&type=" + DEFAULT_TYPE.getValue()
                + "&market=" + DEFAULT_MARKET
                + "&limit=" + DEFAULT_LIMIT;

        return RequestTemplate.requestMapping(HttpMethod.GET, headers, body, url);
    }

    /**
     * 스포티파이 검색 결과 문자열을 파싱하여 Music 리스트를 생성하여 리턴합니다.
     *
     * @param query 검색할 문자열
     * @return List<Music> 검색 결과 Music 리스트
     */
    public List<Music> getSearchResult(String query) {
        Map<String, LinkedHashMap> map = search(query);
        Map<String, ArrayList> tracks = map.get(DEFAULT_TYPE.getValue());
        List<LinkedHashMap> items = tracks.get("items");

//        파싱방법
//        int idx = 0;
//        for (LinkedHashMap el : items) {
//            System.out.printf("%d: \n", idx++);
//            System.out.println(el.get("id"));
//            System.out.println(el.get("name"));
//            Music tmp = Music
//                    .builder()
//                    .spotifyId((String) el.get("id"))
//                    .title((String) el.get("name"))
//                    .build();
//            System.out.println(tmp);
//            System.out.println();
//        }
        return items.stream()
                .map(m -> Music.builder()
                        .spotifyId((String) m.get("id"))
                        .title((String) m.get("name"))
                        .build()
                )
                .toList();
    }
}
