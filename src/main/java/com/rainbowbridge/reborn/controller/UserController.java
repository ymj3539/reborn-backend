package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.user.LoginDto;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     *  회원가입
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserAddDto dto){
        return ResponseEntity.ok(userService.addUser(dto));
    }

    /**
     *  로그인
     */
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginDto dto, HttpSession session) {
        return ResponseEntity.ok(userService.loginUser(dto, session));
    }

}
