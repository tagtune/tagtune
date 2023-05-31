package com.ll.tagtune.base.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestTemplate {

    /**
     * 외부 API 에 요청하고 Body 를 리턴합니다
     *
     * @param httpMethod
     * @param headers    추가할 header 를 add() 하여 추가한다.
     * @param body
     * @param url
     * @return responseEntity.getBody API 요청에 대한 응답의 Body 입니다.
     */
    public static String request(
            final HttpMethod httpMethod,
            final HttpHeaders headers,
            final String body,
            final String url
    ) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders(headers);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = rest
                .exchange(url, httpMethod, requestEntity, String.class);
        return responseEntity.getBody();
    }
}
