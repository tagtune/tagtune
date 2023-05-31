package com.ll.tagtune.base.lastfm;

import com.ll.tagtune.base.api.RequestTemplate;
import com.ll.tagtune.base.appConfig.AppConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchEndpoint {
    public static String musicSearch(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");

        String body = "";

        String url = "http://ws.audioscrobbler.com/2.0/?method=track.search"
                + "&track=" + query
                + "&api_key=" + AppConfig.getLastfmClientId()
                + "&format=json";

        return RequestTemplate.request(HttpMethod.GET, headers, body, url);
    }
}
