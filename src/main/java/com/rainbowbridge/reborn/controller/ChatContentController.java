package com.rainbowbridge.reborn.controller;

import com.google.common.net.HttpHeaders;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentListDto;
import com.rainbowbridge.reborn.service.ChatContentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/chat/contents", produces = "application/json; charset=utf8")
public class ChatContentController {

    private final ChatContentService chatContentService;

    @GetMapping("/{chatRoomId}")
    @ApiOperation(value = "채팅 메세지 리스트 조회")
    public List<ChatContentListDto> getChatMessages(@PathVariable Long chatRoomId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return chatContentService.getChatContentListDto(chatRoomId, token);
    }

}
