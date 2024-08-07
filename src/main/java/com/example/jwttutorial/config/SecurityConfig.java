package com.example.jwttutorial.config;

import com.example.jwttutorial.jwt.JWTFilter;
import com.example.jwttutorial.jwt.JWTUtil;
import com.example.jwttutorial.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf disable
        http
            .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
            .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join").permitAll()
                .anyRequest().authenticated());

        http
            .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // stateless 상태로 관리하기 위해 넣은 구문
        // 세션의 stateless 란?
        // 세션은 서버의 메모리에 유저 정보를 저장하여 동일한 브라우저에서 오는 요청을 기억할 수 있다.
        // 따라서 로그인을 했다면 매번 요청 마다 인증 과정 없이 세션에서 기억하고 있기 때문에 특정 작업을 진행할 수 있는데 이를 막기 위한 방법이 stateless


        return http.build();
    }
}
