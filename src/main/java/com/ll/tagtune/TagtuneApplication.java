package com.ll.tagtune;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TagtuneApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagtuneApplication.class, args);
    }

}
