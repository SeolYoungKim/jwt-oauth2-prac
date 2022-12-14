package com.example.jwt_oauth_prac.sevice;

import com.example.jwt_oauth_prac.domain.Member;
import com.example.jwt_oauth_prac.domain.RoleType;
import com.example.jwt_oauth_prac.jwt.JwtProvider;
import com.example.jwt_oauth_prac.jwt.dto.JwtDto;
import com.example.jwt_oauth_prac.jwt.dto.ReissueRequestDto;
import com.example.jwt_oauth_prac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

import static java.util.Objects.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public JwtDto login(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 회원이 아닐 경우 회원 가입 처리를 한다.
        if (attributes.get("id") != null) {
            if (!memberRepository.existsByAuthId(attributes.get("id").toString())) {

                Member member = Member.builder()
                        .authId((String) attributes.get("id"))
                        .email((String) attributes.get("sub"))
                        .name((String) attributes.get("name"))
                        .picture((String) attributes.get("picture"))
                        .roleType(RoleType.ADMIN)
                        .build();

                memberRepository.save(member);
            }
        }

        return jwtProvider.generateJwtDto(oAuth2User);
    }

    public JwtDto reissue(ReissueRequestDto reissueRequestDto) {

        jwtProvider.validateRefreshToken(reissueRequestDto.getRefreshToken());

        Authentication authentication = jwtProvider.findAuthentication(reissueRequestDto.getRefreshToken());

        return jwtProvider.generateJwtDto(authentication.getName());
    }
}
