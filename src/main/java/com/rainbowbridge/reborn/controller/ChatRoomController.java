package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.dto.chatContent.ChatContentResponseDto;
import com.rainbowbridge.reborn.dto.chatRoom.ChatRoomListDto;
import com.rainbowbridge.reborn.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/chat/rooms", produces = "application/json; charset=utf8")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @ApiOperation(value = "채팅방 목록 조회")
    public List<ChatRoomListDto> getList(HttpSession session) {
        return chatRoomService.getChatRoomList(session);
    }


    @PostMapping("/{companyId}")
    @ApiOperation(value = "상담 하기")
    public ChatContentResponseDto enter(@PathVariable String companyId, HttpSession session) {
        return chatRoomService.enterChatRoom(companyId, session);
    }

}
