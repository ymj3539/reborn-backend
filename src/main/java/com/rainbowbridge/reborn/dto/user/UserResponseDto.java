package com.rainbowbridge.reborn.dto.user;

import com.rainbowbridge.reborn.domain.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @ApiModelProperty(value = "사용자 ID")
    private String id;

    @ApiModelProperty(value = "사용자 이름")
    private String name;

    @ApiModelProperty(value = "사용자 전화번호")
    private String phoneNum;

    @ApiModelProperty(value = "사용자 생일")
    private LocalDate birthday;

    @ApiModelProperty(value = "사용자 성별")
    private String gender;

    @ApiModelProperty(value = "사용자 우편번호")
    private String postalCode;

    @ApiModelProperty(value = "사용자 기본주소")
    private String baseAddress;

    @ApiModelProperty(value = "사용자 상세주소")
    private String detailAddress;

}
