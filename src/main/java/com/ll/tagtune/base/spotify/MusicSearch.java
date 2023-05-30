package com.ll.tagtune.base.spotify;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class MusicSearch {
    public String search(String query) {
        final String accessToken = CreateToken.accessToken();
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = rest
                .exchange(
                        "https://api.spotify.com/v1/search?type=track&q=" + query,
                        HttpMethod.GET,
                        requestEntity,
                        String.class
                );
        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value(); //상태 코드가 들어갈 status 변수
        String response = responseEntity.getBody();
        System.out.println("Response status: " + status);
        System.out.println(response);

        return response;
    }
}
