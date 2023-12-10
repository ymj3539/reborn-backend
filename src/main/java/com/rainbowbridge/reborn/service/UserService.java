package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.user.LoginDto;
import com.rainbowbridge.reborn.dto.user.LoginResponseDto;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void checkDuplicatedId(String id) {
        if (userRepository.findById(id).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional
    public LoginResponseDto addUser(UserAddDto dto, HttpSession session){
        String encodedPassword = UserSha256.encrypt(dto.getPassword());
        User user = dto.toEntity(encodedPassword);

        session.setAttribute("user", userRepository.save(user));
        return toLoginResponseDto(user);
    }

    public LoginResponseDto loginUser(LoginDto dto, HttpSession session) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("아이디가 존재하지 않습니다."));

        String encodedPassword = UserSha256.encrypt(dto.getPassword());
        if (!encodedPassword.equals(user.getPassword())) {
            throw new IllegalStateException("비밀번호가 존재하지 않습니다.");
        }

        session.setAttribute("user", user);

        return toLoginResponseDto(user);
    }

    public LoginResponseDto toLoginResponseDto(User user) {
        return LoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();
    }
}
