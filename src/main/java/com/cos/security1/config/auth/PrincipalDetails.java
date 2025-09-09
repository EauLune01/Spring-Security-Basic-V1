package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.domain.User;

import lombok.Data;
import org.springframework.security.oauth2.core.user.OAuth2User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인의 진행이 완료가 되면 security session을 만들어줌(Security ContextHolder)
//Object의 타입=>Authentication 타입의 객체
//Authentication 안에 User 정보가 있어야 함
//User Object의 타입=>UserDetails 타입의 객체

//Seucurity Session=>Authentication=>UserDetails(PrincipalDetails)
@Data
// Authentication 객체에 저장할 수 있는 유일한 타입
public class PrincipalDetails implements UserDetails, OAuth2User{

    private static final long serialVersionUID = 1L;
    private User user;
    private Map<String, Object> attributes;

    // 일반 시큐리티 로그인시 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2.0 로그인시 사용
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<GrantedAuthority>();
        collet.add(()->{ return user.getRole();});
        return collet;
    }

    // 리소스 서버로 부터 받는 회원정보
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // User의 PrimaryKey
    @Override
    public String getName() {
        return user.getId()+"";
    }

}