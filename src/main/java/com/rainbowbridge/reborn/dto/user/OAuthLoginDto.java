package com.rainbowbridge.reborn.dto.user;

import com.rainbowbridge.reborn.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthLoginDto {

    @NotBlank
    @ApiModelProperty(value = "OAuthProvider에서 받은 AccessToken")
    private String accessToken;

    public User toEntity(String id, String encodedPassword, String name, String phoneNum) {
        return User.builder()
                .id(id)
                .password(encodedPassword)
                .name(name)
                .phoneNum(phoneNum)
                .build();
    }

    public User toEntity(String id, String encodedPassword, String nickname) {
        return User.builder()
                .id(id)
                .password(encodedPassword)
                .name(nickname)
                .build();
    }
}
