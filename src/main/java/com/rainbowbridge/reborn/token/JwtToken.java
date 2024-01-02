package com.rainbowbridge.reborn.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
