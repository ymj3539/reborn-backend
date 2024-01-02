package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.token.JwtToken;
import com.rainbowbridge.reborn.dto.user.UserResponseDto;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.repository.UserRepository;
import com.rainbowbridge.reborn.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackListService tokenBlackListService;

    public User checkUser(String accessToken) {
        if (accessToken == null) {
            return null;
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            return null;
        }

        // 토큰을 검증하고 인증 객체를 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // 인증 객체에서 사용자 정보를 추출합니다.
        String userId = (String) authentication.getPrincipal();

        // 사용자 정보를 데이터베이스에서 조회합니다.
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
    }

    public void checkDuplicatedId(String id) {
        if (userRepository.findById(id).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional
    public UserResponseDto addUser(UserAddDto dto){
        String originalPassword = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(originalPassword);
        User user = dto.toEntity(encodedPassword);
        user.addRole("USER");

        user = userRepository.save(user);

        return toUserResponseDto(user, getToken(user.getId(), originalPassword));
    }

    @Transactional
    public UserResponseDto loginUser(String id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        return toUserResponseDto(user, getToken(id, password));
    }

    public JwtToken getToken(String id, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 반환
        return jwtTokenProvider.generateToken(authentication);
    }

    public void logoutUser(String accessToken) {
        // 토큰에서 "Bearer " 제거
        accessToken = accessToken.replace("Bearer ", "");

        // 토큰에서 만료 시간을 가져와서 저장
        Date expiryDate = jwtTokenProvider.getExpiryDate(accessToken);

        tokenBlackListService.add(accessToken, expiryDate);
    }

    public UserResponseDto toUserResponseDto(User user, JwtToken token) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .address(user.getAddress())
                .accessToken(token.getAccessToken())
                .build();
    }
}
