package com.ll.tagtune.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static String artistUrl;
    @Getter
    private static String spotifyClientId;
    @Getter
    private static String spotifyClientSecret;
    @Getter
    private static String lastfmClientId;
    @Getter
    private static Integer tagPagingSize;

    @Value("${custom.image.artistUrl}")
    public void setArtistUrl(String artistUrl) {
        AppConfig.artistUrl = artistUrl;
    }

    @Value("${custom.spotify.clientId}")
    public void setSpotifyClientId(String spotifyClientId) {
        AppConfig.spotifyClientId = spotifyClientId;
    }

    @Value("${custom.spotify.client-secret}")
    public void setSpotifyClientSecret(String spotifyClientSecret) {
        AppConfig.spotifyClientSecret = spotifyClientSecret;
    }

    @Value("${custom.lastfm.clientId}")
    public void setLastfmClientId(String lastfmClientId) {
        AppConfig.lastfmClientId = lastfmClientId;
    }

    @Value("${custom.tag.pagingSize}")
    public void setTagPagingSize(Integer tagPagingSize) {
        AppConfig.tagPagingSize = tagPagingSize;
    }
}
