package com.cos.security1.controller;

import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cos.security1.domain.User;
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping({"","/"})
    public String index(){
        //머스테치로 view return
        return "index";
    }
    @GetMapping("/user")
    public String user(){
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
