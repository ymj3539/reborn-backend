package com.rainbowbridge.reborn.dto.chatContent;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    @ApiModelProperty(value = "채팅 메세지 pk", example = "")
    private Long chatContentId;

    @ApiModelProperty(value = "사용자 전송 메시지 여부 : true - 사용자 전송 메시지 / false - 업체 전송 메시지", example = "")
    private boolean userSendYN;

    @ApiModelProperty(value = "채팅 메시지", example = "안녕하세요. ‘포포즈’ 상담원입니다. 어떤 도움이 필요하신가요?")
    private String content;

    @ApiModelProperty(value = "아직 읽지 않은 사람 수", example = "")
    private int readCount;
}
