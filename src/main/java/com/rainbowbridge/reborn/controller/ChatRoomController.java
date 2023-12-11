package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.chatContent.ChatContentResponseDto;
import com.rainbowbridge.reborn.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/chat/rooms", produces = "application/json; charset=utf8")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/{companyId}")
    @ApiOperation(value = "상담 하기")
    public ChatContentResponseDto enter(@PathVariable String companyId, HttpSession session) {
        return chatRoomService.enterChatRoom(companyId, session);
    }

}
