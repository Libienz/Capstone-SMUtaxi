package com.capstone.smutaxi.chat;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

/**
 * 채팅방을 위한 DTO
 * 입장한 클라이언트들의 정보를 가지고있어야 하므로 WebSocketSession 정보 리스트를 멤버필드로 가짐
 *
 */
@Getter
public class MsgRoom {
    private String roomId; // 채팅방 아이디
    private String name; // 채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>(); //채팅룸 세션 = 클라이언트세션리스트

    @Builder
    public MsgRoom(String roomId, String name){
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerAction(WebSocketSession session, Message message, MsgService msgService){
        if(message.getMessageType().equals(Message.MessageType.ENTER)){
            sessions.add(session);  //클라이언트의 세션 추가
            message.setMessage(message.getSender() +"님이 입장했습니다.");
        }
        sendMessage(message,msgService); //채팅방에 메세지가 도착하면 채팅방의 모든 세션에 메세지 발송

    }

    public <T> void sendMessage(T message, MsgService msgService){
        sessions.parallelStream().forEach(session -> msgService.sendMessage(session,message));
    }


}
