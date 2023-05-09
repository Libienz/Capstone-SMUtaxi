package com.capstone.smutaxi.chat;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    /**
     *소켓 통신은 기본적으로 서버와 클라이언트 간 일대다 관계를 맺는다
     * 한 서버에서 여러 클라이언트가 접속할수있으므로, 서버는 여러 클라이언트가 발송한 메세지를 받아서 처리할 Handler가 필요
     */

    //handler가 client로 환영 메세지를 보내는 모습
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        TextMessage initialGreeting = new TextMessage("Welcome to Chat Server");
        session.sendMessage(initialGreeting);
    }
}
