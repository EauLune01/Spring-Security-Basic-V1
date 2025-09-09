package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cos.security1.domain.User;
@Controller
@RequiredArgsConstructor

/**
 * @AuthenticationPrincipal: Spring Security에서 현재 인증된 사용자 정보를 컨트롤러 메서드에 주입할 때 사용하는 애노테이션
 * */
public class IndexController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login==================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication: "+principalDetails.getUser());

        System.out.println("userDetails: "+userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/auth/login")
    @ResponseBody
    public String testOAuthLogin(Authentication authentication,
                                 @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/oauth/login==================");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: "+oauth2User.getAttributes());
        System.out.println("oauth2User:"+oauth2User.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }
    @GetMapping({"","/"})
    public String index(){
        //머스테치로 view return
        return "index";
    }

    //OAuth 로그인을 해도, 일반 로그인을 해도 PrincipalDetails 타입으로 받을 수 있음
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails: "+principalDetails.getUser());
        return "user";
    }
    @GetMapping("/admin") //권한
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager") //권한
    public String manager(){
        return "manager";
    }
    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }
    @GetMapping("/joinForm") //회원가입
    public String joinForm(){
        return "joinForm";
    }
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword=bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        return "개인 정보";
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')") //data 메서드 실행되기 직전에 실행, 내부적으로 자동으로 "ROLE_" prefix가 붙음
    //@PostAuthorize(): data 메서드 종료 후 실행
    @GetMapping("/data")
    @ResponseBody
    public String data(){
        return "데이터 정보";
    }
}
