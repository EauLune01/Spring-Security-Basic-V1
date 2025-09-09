package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IoC 빈(bean) 등록
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // 메서드 단위 권한 제어 활성화 (@PreAuthorize, @Secured 같은 메서드 수준의 권한 체크 애노테이션 활성화)
/**
 * prePostEnabled=true->preAuthorize, postAuthorize 애노테이션 활성화
 * */
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").hasRole("MANAGER") // "ROLE_MANAGER"으로 매핑
                .requestMatchers("/admin/**").hasRole("ADMIN") // "ROLE_ADMIN"으로 매핑
                .anyRequest().permitAll()
        );

        //로그인 page로 redirect
        http.formLogin(form -> form
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") //login 주소가 호출이 되면 security가 낚아채서 대신 로그인을 진행->로그인 컨트롤러 안 만들어도 됨
                .defaultSuccessUrl("/") //메인 페이지로 이동
        );

        return http.build();
    }
}
