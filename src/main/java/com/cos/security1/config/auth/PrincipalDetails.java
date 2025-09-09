package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.domain.User;

import lombok.Data;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인의 진행이 완료가 되면 security session을 만들어줌(Security ContextHolder)
//Object의 타입=>Authentication 타입의 객체
//Authentication 안에 User 정보가 있어야 함
//User Object의 타입=>UserDetails 타입의 객체

//Seucurity Session=>Authentication=>UserDetails(PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails{

    private User user;

    public PrincipalDetails(User user) {
        super();
        this.user = user;
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

    //해당 User의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> user.getRole()); // ROLE_USER, ROLE_ADMIN
        return authorities;
    }

}
