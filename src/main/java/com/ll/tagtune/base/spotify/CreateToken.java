package com.ll.tagtune.base.spotify;

import com.ll.tagtune.base.appConfig.AppConfig;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;

public class CreateToken {
    private static final SpotifyApi spotifyApi;

    static {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(AppConfig.getSpotifyClientId())
                .setClientSecret(AppConfig.getSpotifyClientSecret())
                .build();
    }

    public static String accessToken() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi.getAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("[ERROR] : " + e.getMessage());
            return "error";
        }
    }
}
