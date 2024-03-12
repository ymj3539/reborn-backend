package com.rainbowbridge.reborn.dto.chatRoom;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyChatRoomDto {
    @ApiModelProperty(value = "유저 pk", example = "")
    private String userId;

    @ApiModelProperty(value = "유저 이름", example = "김철수")
    private String userName;

    @ApiModelProperty(value = "업체 Pk", example = "포포즈 경기점")
    private Long companyId;

    @ApiModelProperty(value = "예약 중 여부", example = "true")
    private boolean reservationYN;

    @ApiModelProperty(value = "예약 Pk", example = "")
    private Long reservationId;

    @ApiModelProperty(value = "예약일", example = "2024.01.03(수)")
    private String reservationDate;

    @ApiModelProperty(value = "예약 패키지 이름", example = "라이언 패키지")
    private String bundleName;
}