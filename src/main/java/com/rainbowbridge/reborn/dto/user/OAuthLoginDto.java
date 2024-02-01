package com.rainbowbridge.reborn.dto.user;

import com.rainbowbridge.reborn.domain.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthLoginDto {

    private String accessToken;

    public User toEntity(String id, String encodedPassword) {
        return User.builder()
                .id(id)
                .password(encodedPassword)
                .build();
    }
}
