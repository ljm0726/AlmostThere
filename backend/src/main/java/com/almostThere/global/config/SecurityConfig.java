package com.almostThere.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .httpBasic().disable()      // Http basic Auth  기반으로 로그인 인증창이 뜸.  disable 시에 인증창 뜨지 않음.
            .csrf().disable()       // rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .formLogin().disable() // 로그인 폼 미사용
            .httpBasic().disable() // Http basic Auth 기반으로 열리는 로그인 인증창 미사용
            .sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)// jwt token으로 인증하므로 세션 사용하지 않음. stateless 하도록 처리.
            .and()
            .authorizeRequests()
            .antMatchers("/api/token/**", "/oauth2/**", "/api-docs/**", "/swagger-ui/**").permitAll(); //토큰 재발급 요청은 제외

        return http.build();
    }



}
