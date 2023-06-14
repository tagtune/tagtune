package com.ll.tagtune.base;

import com.ll.tagtune.base.appConfig.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppConfigTest {
    @Test
    @DisplayName("BaseUrl")
    void baseUrlTest() throws Exception {
        System.out.println(AppConfig.getArtistUrl());
    }
}
