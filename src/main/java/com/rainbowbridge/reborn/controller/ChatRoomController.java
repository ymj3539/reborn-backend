package com.rainbowbridge.reborn.controller;

import com.google.common.net.HttpHeaders;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentResponseDto;
import com.rainbowbridge.reborn.dto.chatRoom.ChatRoomDto;
import com.rainbowbridge.reborn.dto.chatRoom.ChatRoomListDto;
import com.rainbowbridge.reborn.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/chat/rooms", produces = "application/json; charset=utf8")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @ApiOperation(value = "채팅방 목록 조회")
    public List<ChatRoomListDto> getList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return chatRoomService.getChatRoomList(token);
    }

    @GetMapping("/{chatRoomId}")
    @ApiOperation(value = "채팅방 조회")
    public ChatRoomDto getchatRoom(@PathVariable Long chatRoomId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return chatRoomService.getChatRoom(chatRoomId, token);
    }


    @PostMapping("/{companyId}")
    @ApiOperation(value = "상담 하기")
    public ChatRoomDto enter(@PathVariable Long companyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return chatRoomService.enterChatRoom(companyId, token);
    }

}
