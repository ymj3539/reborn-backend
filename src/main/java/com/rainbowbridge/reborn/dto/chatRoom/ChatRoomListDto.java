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
public class ChatRoomListDto {

    @ApiModelProperty(value = "채팅방 ID", example = "")
    private Long chatRoomId;

    @ApiModelProperty(value = "업체 ID", example = "")
    private Long companyId;

    @ApiModelProperty(value = "업체 이름", example = "포포즈 경기 김포점")
    private String companyName;

    @ApiModelProperty(value = "업체 이미지 경로", example = "")
    private String companyImagePath;

    @ApiModelProperty(value = "예약 중 여부", example = "")
    private boolean reservationYN;

    @ApiModelProperty(value = "최근 채팅 메시지", example = "동행서비스의 경우 편도 기준의 서비스입니다. 장례를 마친 후에는 포포즈의 의전 차량 또는 일반 택시 사용에 대해 추가로 안내해 드리고 있습니다.")
    private String recentChat;

}
