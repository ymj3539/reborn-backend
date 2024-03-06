package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.chatContent.ChatMessageDto;
import com.rainbowbridge.reborn.service.ChatContentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/chatting", produces = "application/json; charset=utf8")
public class ChatSocketController {

    private final ChatContentService chatContentService;
    /*
    *   채팅방 소켓연결
     */
    @MessageMapping("/message/{chatRoomId}") // 여기로 보낸 메시지를
    @SendTo("/topic/message/{chatRoomId}")  // 여기로 보내줌
    @ApiOperation(value = "채팅 메세지 보내기(소켓)")
    public ChatMessageDto chatting(ChatMessageDto message,
                                   @DestinationVariable Long chatRoomId){
        // 메세지 디비 저장
        chatContentService.addUserChat(message, chatRoomId);
        return message;
    }
}
