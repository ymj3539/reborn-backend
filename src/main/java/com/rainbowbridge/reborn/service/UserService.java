package com.rainbowbridge.reborn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.user.*;
import com.rainbowbridge.reborn.exception.CustomException;
import com.rainbowbridge.reborn.token.JwtToken;
import com.rainbowbridge.reborn.repository.UserRepository;
import com.rainbowbridge.reborn.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackListService tokenBlackListService;
    private final String KAKAO_REQ_URL = "https://kapi.kakao.com/v2/user/me";
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional(readOnly = true)
    public User checkUser(String accessToken) {
        if (accessToken == null) {
            return null;
        }

        // 토큰에서 "Bearer " 제거
        accessToken = accessToken.replace("Bearer ", "");

        if (!jwtTokenProvider.validateToken(accessToken)) {
            return null;
        }

        // 토큰을 검증하고 인증 객체를 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // 인증 객체에서 사용자 정보를 추출합니다.
        String userId = ((UserDetails)authentication.getPrincipal()).getUsername();

        // 사용자 정보를 데이터베이스에서 조회합니다.
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
    }

    public UserResponseDto getUser(String accessToken) {
        return toUserResponseDto(checkUser(accessToken));
    }

    public UserResponseDto updateUser(String accessToken, UserUpdateDto dto) {
        User user = checkUser(accessToken);
        user.updateInfo(dto);
        return toUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public void checkDuplicatedId(String id) {
        if (userRepository.existsById(id)) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    public LoginResponseDto addUser(UserAddDto dto){
        String originalPassword = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(originalPassword);
        User user = dto.toEntity(encodedPassword);
        user.addRole("USER");

        user = userRepository.save(user);

        return toLoginResponseDto(user, getToken(user.getId(), originalPassword));
    }

    public LoginResponseDto loginUser(String id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사번입니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        return toLoginResponseDto(user, getToken(id, password));
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
        if (accessToken == null) {
            throw new IllegalArgumentException("액세스 토큰이 필요합니다.");
        }

        // 토큰에서 "Bearer " 제거
        accessToken = accessToken.replace("Bearer ", "");

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 토큰에서 만료 시간을 가져와서 저장
        Date expiryDate = jwtTokenProvider.getExpiryDate(accessToken);

        tokenBlackListService.add(accessToken, expiryDate);
    }


    public LoginResponseDto OAuthLoginUser(OAuthLoginDto dto) {
        String reqURL = KAKAO_REQ_URL;

        // 1. accessToken으로 사용자 정보 조회
        // Http Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + dto.getAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Http 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                reqURL,
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        JsonNode jsonNode = null;
        try {
            String responseBody = response.getBody();
            ObjectMapper objMapper = new ObjectMapper();
            jsonNode = objMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            logger.info("json 처리 에러, jsonNode : {}", jsonNode);
            throw new CustomException("사용자 정보 가져오기에 실패하였습니다. ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String socialId = jsonNode.get("id").asText(); // 카카오 회원번호

        // password: random UUID
        String password = UUID.randomUUID().toString();

        // 2. DB에 존재하는 회원인지 확인
        User SocialUser = userRepository.findById(socialId)
                .orElse(null);

        // 3. 존재하지 않는 사용자면 회원가입 후 로그인
        if (SocialUser == null) {
            String encodedPassword = passwordEncoder.encode(password);
            User user = dto.toEntity(socialId, encodedPassword);
            user.addRole("USER");

            user = userRepository.save(user);

            return toLoginResponseDto(user, getToken(user.getId(), password));
        }

        // 4. 존재하면 로그인
        else {
            return toLoginResponseDto(SocialUser, getToken(socialId, password));
        }


    }

    public LoginResponseDto toLoginResponseDto(User user, JwtToken token) {
        return LoginResponseDto.builder()
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .accessToken(token.getAccessToken())
                .build();
    }

    private UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .birthday(user.getBirthday())
                .gender(user.getGender().getDisplayName())
                .postalCode(user.getPostalCode())
                .baseAddress(user.getBaseAddress())
                .detailAddress(user.getDetailAddress())
                .build();
    }
}
