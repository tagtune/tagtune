package com.ll.tagtune.base.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/usr/member/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/usr/member/login")
                                .defaultSuccessUrl("/test")

                )
                .logout(
                        logout -> logout
                                .logoutUrl("/usr/member/logout")
//                                .logoutSuccessUrl("/usr/member/login") // 로그아웃 후 이동할 URL
//                                .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
//                                .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키 설정
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
