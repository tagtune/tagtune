package com.ll.tagtune.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static String artistUrl;

    @Value("${custom.image.artistUrl}")
    public void setArtistUrl(String artistUrl) {
        AppConfig.artistUrl = artistUrl;
    }
}
