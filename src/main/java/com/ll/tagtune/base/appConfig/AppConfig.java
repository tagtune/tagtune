package com.ll.tagtune.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static String artistUrl;
    @Getter
    private static String lastfmClientId;
    @Getter
    private static Integer tagPagingSize;
    @Getter
    private static String nameForNoData;
    @Getter
    private static Integer recommendSize;

    @Value("${custom.image.artistUrl}")
    public void setArtistUrl(String artistUrl) {
        AppConfig.artistUrl = artistUrl;
    }

    @Value("${custom.lastfm.clientId}")
    public void setLastfmClientId(String lastfmClientId) {
        AppConfig.lastfmClientId = lastfmClientId;
    }

    @Value("${custom.tag.pagingSize}")
    public void setTagPagingSize(Integer tagPagingSize) {
        AppConfig.tagPagingSize = tagPagingSize;
    }

    @Value("${custom.data.nodata}")
    public void setTagPagingSize(String nameForNoData) {
        AppConfig.nameForNoData = nameForNoData;
    }

    @Value("${custom.track.recommendSize}")
    public void setRecommendSize(Integer recommendSize) {
        AppConfig.recommendSize = recommendSize;
    }
}
