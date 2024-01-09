package com.rainbowbridge.reborn.dto.user;

import com.rainbowbridge.reborn.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddDto {

    @NotBlank
    private String id;

    @NotBlank
    private String password;    // 비밀번호

    @NotBlank
    private String name;        // 이름

    @NotBlank
    private String phoneNum;    // 전화번호

    @NotBlank
    private String postalCode;  // 우편번호

    @NotEmpty
    private String baseAddress;     // 기본 주소

    @NotEmpty
    private String detailAddress;   // 상세 주소

    public User toEntity(String encodedPassword) {
        return User.builder()
                .id(id)
                .password(encodedPassword)
                .name(name)
                .phoneNum(phoneNum)
                .postalCode(postalCode)
                .baseAddress(baseAddress)
                .detailAddress(detailAddress)
                .build();
    }
}
