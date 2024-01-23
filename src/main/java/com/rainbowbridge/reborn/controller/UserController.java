package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.dto.user.LoginDto;
import com.rainbowbridge.reborn.dto.user.LoginResponseDto;
import com.rainbowbridge.reborn.dto.user.UserResponseDto;
import com.rainbowbridge.reborn.dto.user.UserAddDto;
import com.rainbowbridge.reborn.dto.user.UserUpdateDto;
import com.rainbowbridge.reborn.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/users", produces = "application/json; charset=utf8")
public class UserController {

    private final UserService userService;

    @GetMapping("/check/{id}")
    @ApiOperation(value="아이디 중복 확인")
    public ResponseEntity checkDuplicatedId(@PathVariable String id) {
        userService.checkDuplicatedId(id);
        return Utils.createResponse("사용 가능한 아이디입니다.", HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value="회원가입")
    public LoginResponseDto addUser(@RequestBody UserAddDto dto){
        return userService.addUser(dto);
    }

    @GetMapping("/check/login")
    @ApiOperation(value="로그인 여부 확인")
    public ResponseEntity checkLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        if (userService.checkUser(accessToken) != null) {
            return Utils.createResponse("유효한 토큰입니다.", HttpStatus.OK);
        } else {
            return Utils.createResponse("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    @ApiOperation(value="로그인")
    public LoginResponseDto loginUser(@RequestBody LoginDto dto) {
        return userService.loginUser(dto.getId(), dto.getPassword());
    }

    @PostMapping("/logout")
    @ApiOperation(value="로그아웃")
    public ResponseEntity logoutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        userService.logoutUser(accessToken);
        return Utils.createResponse("로그아웃에 성공하였습니다.", HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value="사용자 정보 조회")
    public UserResponseDto getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        return userService.getUser(accessToken);
    }

    @PutMapping
    @ApiOperation(value="사용자 정보 수정")
    public UserResponseDto updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
                                      @RequestBody UserUpdateDto dto) {
        return userService.updateUser(accessToken, dto);
    }

}
