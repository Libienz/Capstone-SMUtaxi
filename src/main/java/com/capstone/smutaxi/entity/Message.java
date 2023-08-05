package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.dto.MessageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendTime;

    private String message;

    private String senderProfileImageUrl;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public MessageDto toMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageId(this.id);
        messageDto.setSenderId(this.senderEmail);
        messageDto.setSenderName(this.senderName);
        messageDto.setSenderProfileImageUrl(this.senderProfileImageUrl);
        messageDto.setMessage(this.message);
        messageDto.setSentTime(this.sendTime);
        return messageDto;

    }
}
