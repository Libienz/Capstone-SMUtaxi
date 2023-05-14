package com.capstone.smutaxi.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket //소켓 활성화
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final ChatWebSocketHandler webSocketHandler;

    /**
     * WebSocket에 접속하기 위한 endpoint를 "ws/chat"으로 설정하고
     * 도메인이 다른서버에서도 접속가능하도록 .setAllowedOrigins("*") 설정에 추가.
     * 참고로 스프링 시큐리티 켜져있으면 simple web socket client (크롬 확장프로그램) 이거로 테스트가 안되어서 일단 빌드에서 주석처리하고 진행함
     */

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler,"ws/chat").setAllowedOrigins("*");
    }

}
