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
public class CompanyChatRoomListDto {

    @ApiModelProperty(value = "채팅방 ID", example = "")
    private Long chatRoomId;

    @ApiModelProperty(value = "유저 ID", example = "example@naver.com")
    private String userId;

    @ApiModelProperty(value = "업체 ID", example = "")
    private Long companyId;

    @ApiModelProperty(value = "유저 이름", example = "김철수")
    private String userName;

    @ApiModelProperty(value = "예약 중 여부", example = "")
    private boolean reservationYN;

    @ApiModelProperty(value = "최근 채팅 메세지 시간", example = "오후 8:00")
    private String recentChatTime;

    @ApiModelProperty(value = "최근 채팅 메시지", example = "안녕하세요?")
    private String recentChat;

}