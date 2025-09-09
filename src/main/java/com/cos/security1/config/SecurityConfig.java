package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IoC 빈(bean) 등록
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // 메서드 단위 권한 제어 활성화 (@PreAuthorize, @Secured 같은 애노테이션 사용)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOauth2UserService;  // principalOauth2UserService 주입

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()); // CSRF 비활성화 (로그인 폼 등을 사용하는 경우 필요)

        // URL 패턴에 따른 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").authenticated() // /user/** 경로는 인증된 사용자만 접근 가능
                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") // /manager/** 경로는 ROLE_MANAGER 또는 ROLE_ADMIN 권한이 있어야 접근
                .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/** 경로는 ROLE_ADMIN 권한이 있어야 접근
                .anyRequest().permitAll() // 그 외의 요청은 모두 허용
        );

        // 로그인 페이지 설정
        http.formLogin(form -> form
                .loginPage("/loginForm") // 로그인 페이지 경로
                .loginProcessingUrl("/login") // 로그인 처리 URL
                .defaultSuccessUrl("/") // 로그인 성공 후 리디렉션 경로
        );

        // OAuth2 로그인 설정 (구글, 깃허브 등 소셜 로그인)
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리가 필요
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(principalOauth2UserService) // 사용자가 정의한 OAuth2UserService
                )
        );

        return http.build(); // 설정된 보안 필터 체인 반환
    }
}
