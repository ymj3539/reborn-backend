package com.rainbowbridge.reborn.controller;

import com.rainbowbridge.reborn.Utils;
import com.rainbowbridge.reborn.dto.chatContent.ChatContentAddRequestDto;
import com.rainbowbridge.reborn.service.ChatContentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/chat/contents", produces = "application/json; charset=utf8")
public class ChatContentController {

    private final ChatContentService chatContentService;

    @PostMapping
    @ApiOperation(value = "사용자 채팅 전송")
    public ResponseEntity add(@RequestBody ChatContentAddRequestDto dto, HttpSession session) {
        chatContentService.addUserChat(dto, session);
        return ResponseEntity.ok(Utils.convertMsgToMap("메시지 전송에 성공했습니다."));
    }

}
