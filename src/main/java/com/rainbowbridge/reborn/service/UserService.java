package com.rainbowbridge.reborn.service;

import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User addUser(UserAddDto dto){
        String encodedPassword = UserSha256.encrypt(dto.getPassword());
        User user = dto.toEntity(encodedPassword);
        return userRepository.save(user);
    }
}
