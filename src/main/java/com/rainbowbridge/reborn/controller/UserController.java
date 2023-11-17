package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
