package com.ll.tagtune.base.spotify;

import com.ll.tagtune.boundedContext.music.entity.Music;
import com.ll.tagtune.standard.util.Ut;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MusicSearch {
    private static final String DEFAULT_MARKET = "KR";
    private static final String DEFAULT_TYPE = "track";
    private static final int DEFAULT_LIMIT = 5;

    /**
     * 스포티파이 검색 엔드포인트를 호출합니다
     *
     * @param query 검색할 문자열
     * @return 검색 결과 Json String
     */
    private String search(String query) {
        final String accessToken = CreateToken.getAccessToken();
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = rest
                .exchange(
                        "https://api.spotify.com/v1/search?&q=" + query
                                + "&type=" + DEFAULT_TYPE
                                + "&market=" + DEFAULT_MARKET
                                + "&limit=" + DEFAULT_LIMIT,
                        HttpMethod.GET,
                        requestEntity,
                        String.class
                );
        return responseEntity.getBody();
    }

    /**
     * 스포티파이 검색 결과 문자열을 파싱하여 Music 리스트를 생성하여 리턴합니다.
     *
     * @param query 검색할 문자열
     * @return List<Music> 검색 결과 Music 리스트
     */
    public List<Music> getSearchResult(String query) {
        final String searchJsonResult = search(query);
        Map<String, LinkedHashMap> map = Ut.json.toMap(searchJsonResult);
        Map<String, ArrayList> tracks = map.get("tracks");
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
