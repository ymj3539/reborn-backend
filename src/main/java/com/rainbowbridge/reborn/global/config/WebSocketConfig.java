package com.rainbowbridge.reborn.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatPreHandler chatPreHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 구독 주소
        registry.setApplicationDestinationPrefixes("/app"); // 메세지를 보내는 주소
    }

    /*
    * 프론트에서 SockJS 연결시 사용되는 endpoint
    */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할 수 있게 함
        registry.addEndpoint("/chatting").setAllowedOriginPatterns("*").withSockJS();

        // 앱 동작을 위해 withSockJS() 설정을 뺌
        registry.addEndpoint("/chatting").setAllowedOriginPatterns("*");
    }

    /*
    * [클라이언트 -> 서버]로 들어오는 요청에 인터셉터 설정 (handler를 통한 jwt 유효성 검증)
    * */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(chatPreHandler);
    }
}
