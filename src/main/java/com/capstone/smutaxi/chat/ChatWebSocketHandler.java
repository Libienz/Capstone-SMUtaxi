package com.capstone.smutaxi.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MsgService msgService;


    /**
     *소켓 통신은 기본적으로 서버와 클라이언트 간 일대다 관계를 맺는다
     * 한 서버에서 여러 클라이언트가 접속할수있으므로, 서버는 여러 클라이언트가 발송한 메세지를 받아서 처리할 Handler가 필요
     */

    //handler가 client로 환영 메세지를 보내는 모습
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}",payload);

//        TextMessage initialGreeting = new TextMessage("Welcome to Chat Server");
//        session.sendMessage(initialGreeting);

        Message chatMessage = objectMapper.readValue(payload,Message.class);
        MsgRoom room = msgService.findRoomById(chatMessage.getRoomId());
        room.handlerAction(session,chatMessage,msgService);
    }
}
