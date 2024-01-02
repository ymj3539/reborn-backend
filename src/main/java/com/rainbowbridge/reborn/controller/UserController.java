package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.dto.token.JwtToken;
import com.rainbowbridge.reborn.dto.user.LoginDto;
import com.rainbowbridge.reborn.dto.user.UserResponseDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/users", produces = "application/json; charset=utf8")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value="사용자 정보 조회")
    public UserResponseDto get(@RequestParam(required = false, defaultValue = "") String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="아이디 중복 확인")
    public ResponseEntity checkDuplicatedId(@PathVariable String id) {
        userService.checkDuplicatedId(id);
        return ResponseEntity.ok(Utils.convertMsgToMap("사용 가능한 아이디입니다."));
    }

    @PostMapping
    @ApiOperation(value="회원가입")
    public JwtToken addUser(@RequestBody UserAddDto dto){
        return userService.addUser(dto);
    }

    @PostMapping("/login")
    @ApiOperation(value="로그인")
    public JwtToken loginUser(@RequestBody LoginDto dto) {
        return userService.loginUser(dto.getId(), dto.getPassword());
    }

}
