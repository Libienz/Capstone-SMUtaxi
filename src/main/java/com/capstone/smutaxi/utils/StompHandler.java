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

        }
        if(accessor.getCommand()==StompCommand.SUBSCRIBE){
            String destination = accessor.getDestination();
            System.out.println("구독됨, destination = " + destination);

        }
        if(accessor.getCommand()==StompCommand.CONNECT){
            System.out.println("서버와 웹소켓 연결됨");

        }
        if(accessor.getCommand()==StompCommand.UNSUBSCRIBE){
            String destination = accessor.getDestination();
            System.out.println("구독해제됨, destination = " + destination);
        }
        return message;
    }
}
