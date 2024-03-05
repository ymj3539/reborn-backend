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
public class ChatRoomDto {
    @ApiModelProperty(value = "업체 pk", example = "")
    private Long companyId;

    @ApiModelProperty(value = "업체 이름", example = "포포즈 경기 김포점")
    private String companyName;

    @ApiModelProperty(value = "업체 이미지 경로", example = "")
    private String companyImagePath;

    @ApiModelProperty(value = "예약 중 여부", example = "")
    private boolean reservationYN;

    @ApiModelProperty(value = "예약일", example = "11.15(수)")
    private String reservationDate;
}