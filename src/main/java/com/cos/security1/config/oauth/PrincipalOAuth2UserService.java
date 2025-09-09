package com.cos.security1.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 구글 로그인 프로필 조회
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // code를 통해 구성한 정보
        String provider = userRequest.getClientRegistration().getClientId(); // 예: google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("defaultPassword"); // 예시 비밀번호
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        // 이미 사용자 존재하는지 확인
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            // 사용자 존재하지 않으면 새로 등록
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes()); // 인증된 사용자 반환
    }
}
