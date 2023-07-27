package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.dto.ChatRoomDto;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Entity
@Getter
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderEmail;

    private String senderName;

    private String sendTime;

    private String message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public ChatRoomDto.MessageDto toMessageDto() {
        ChatRoomDto.MessageDto messageDto = new ChatRoomDto.MessageDto();
        messageDto.setId(this.id);
        messageDto.setSenderEmail(this.senderEmail);
        messageDto.setSenderName(this.senderName);
        messageDto.setSendTime(this.sendTime);
        messageDto.setMessage(this.message);
        return messageDto;

    }
}
