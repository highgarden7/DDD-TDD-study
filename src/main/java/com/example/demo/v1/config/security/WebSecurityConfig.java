package com.example.demo.v1.config.security;

import com.example.demo.v1.config.jwt.JwtAuthorizationFilter;
import com.example.demo.v1.config.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //-> 시큐리티 활성화 => 기본 스프링 필터체인에 등록
public class WebSecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtProvider jwtProvider, CustomUserDetailServiceImpl userDetailService) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //jSessionId 사용 거부
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/signup").permitAll()
                        .requestMatchers("/api/v1/user/login").permitAll()
                        .requestMatchers("/api/v1/api-docs/**", "/swagger-ui/**").permitAll() // swagger
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthorizationFilter(jwtProvider, userDetailService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
