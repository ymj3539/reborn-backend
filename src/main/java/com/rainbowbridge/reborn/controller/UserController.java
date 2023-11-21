package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.domain.User;
import com.rainbowbridge.reborn.dto.user.LoginDto;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ApiOperation(value="아이디 중복 확인")
    public ResponseEntity checkDuplicatedId(@PathVariable String id) {
        userService.checkDuplicatedId(id);
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping
    @ApiOperation(value="회원가입")
    public ResponseEntity addUser(@RequestBody UserAddDto dto, HttpSession session){
        userService.addUser(dto, session);
        return ResponseEntity.ok("로그인에 성공했습니다");
    }

    @PostMapping("/login")
    @ApiOperation(value="로그인")
    public ResponseEntity loginUser(@RequestBody LoginDto dto, HttpSession session) {
        userService.loginUser(dto, session);
        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @GetMapping("/logout")
    @ApiOperation(value="로그아웃")
    public ResponseEntity logout(HttpSession session) {
        session.removeAttribute("user");
        return ResponseEntity.ok("로그아웃에 성공했습니다.");
    }

}
