package com.ll.tagtune.base.spotify;

import com.ll.tagtune.base.appConfig.AppConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateToken {
    private static final SpotifyApi spotifyApi;
    private static LocalDateTime expireDate;

    static {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(AppConfig.getSpotifyClientId())
                .setClientSecret(AppConfig.getSpotifyClientSecret())
                .build();
    }

    /**
     * 스포티파이 엑세스 토큰 생성 메소드
     * clientCredentials 의 expiresIn 의 값을 받아 현재시간에 더해 expireDate 에 저장
     * expireDate 가 현재시간 보다 이전이라면, 토큰 만료 됨
     *
     * @return 스포티파이 엑세스 토큰 생성 결과
     */
    private static boolean setAccessToken() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            expireDate = LocalDateTime.now().plusSeconds(clientCredentials.getExpiresIn());
            return true;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("[ERROR] : " + e.getMessage());
            return false;
        }
    }

    /**
     * @return 스포티파이 엑세스 토큰
     */
    public static String getAccessToken() {
        if (expireDate == null || expireDate.isBefore(LocalDateTime.now())) setAccessToken();
        return spotifyApi.getAccessToken();
    }
}
