package com.capstone.smutaxi.chat;


import lombok.Getter;
import lombok.Setter;

/**
 * 채팅메세지를 주고받기 위한 DTO
 */
@Getter
@Setter
public class Message {
    // 메세지 타입: 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType messageType; //메세지 타입
    private String roomId; //채팅방 아이디
    private String sender; //메세지 보낸사람
    private String message; //메세지
}
