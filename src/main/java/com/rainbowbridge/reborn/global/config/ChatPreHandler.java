package com.rainbowbridge.reborn.global.config;

import com.rainbowbridge.reborn.token.JwtTokenProvider;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    /*
    * websocket을 통해 들어온 요청이 처리 되기전 실행됨
    * */
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

        StompCommand command = headerAccessor.getCommand();


        if(command.equals(StompCommand.UNSUBSCRIBE) || command.equals(StompCommand.MESSAGE) ||
                command.equals(StompCommand.CONNECTED) || command.equals(StompCommand.SEND))
        return message;
            else if (command.equals(StompCommand.ERROR)) {
            throw new MessageDeliveryException("error");
        }

        if(authorizationHeader == null || authorizationHeader.equals("null")){
            log.info("chat header가 없는 요청입니다.");
            throw new MalformedJwtException("jwt");
        }

        // 토큰 자르기
        String token = authorizationHeader.substring(7);

        // 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new MalformedJwtException("jwt Expired");
        }

        return message;

    }

}
