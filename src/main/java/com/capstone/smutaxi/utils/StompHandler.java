package com.capstone.smutaxi.utils;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.DISCONNECT){
            String destination = accessor.getDestination();
            System.out.println("연결종료");
            //이곳에서 매칭 종료시 어떤식으로 채팅방을 처리할지 생각
            //구독 종료시 유저 리스트에서 제거하려면 해당 chatroom id와 유저의id가 필요할텐데 그럼 어떻게받지?
            //구독종료할때 클라이언트에서 stomp헤더에 추가 헤더를달아서 채팅방id를 보내면 괜찮을까?

        }
        return message;
    }
}
