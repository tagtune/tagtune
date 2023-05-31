package com.ll.tagtune.base;

import com.ll.tagtune.base.lastfm.SearchEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LastfmTest {
    @Test
    @DisplayName("Search Test")
    void t001() throws Exception {
        String result = SearchEndpoint.musicSearch("아이유");
        System.out.println(result);
        assertThat(result).contains("아이유");
    }
}
