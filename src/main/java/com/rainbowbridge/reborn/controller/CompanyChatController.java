package com.rainbowbridge.reborn.controller;

import com.google.common.net.HttpHeaders;
import com.rainbowbridge.reborn.dto.chatRoom.CompanyChatRoomDto;
import com.rainbowbridge.reborn.dto.chatRoom.CompanyChatRoomListDto;
import com.rainbowbridge.reborn.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/company/chat/rooms", produces = "application/json; charset=utf8")
public class CompanyChatController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{companyId}")
    @ApiOperation(value = "채팅방 목록 조회(업체용)")
    public List<CompanyChatRoomListDto> getList(@PathVariable Long companyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return chatRoomService.getChatRoomList(companyId);
    }

    @GetMapping("detail/{chatRoomId}")
    @ApiOperation(value = "채팅방 상세 조회(업체용)")
    public CompanyChatRoomDto getChatRoom(@PathVariable Long chatRoomId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return chatRoomService.getCompanyChatRoom(chatRoomId);
    }

}