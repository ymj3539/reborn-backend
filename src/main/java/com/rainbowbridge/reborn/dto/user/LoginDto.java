package com.rainbowbridge.reborn.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    private String id;

    @NotBlank
    private String password;

}
