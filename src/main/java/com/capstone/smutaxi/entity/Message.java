package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.dto.MessageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type")
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "message_id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime sendTime;

    private String message;

    private String senderName;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public MessageDto toMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageId(this.id);
        messageDto.setSenderName(this.senderName);
        messageDto.setMessage(this.message);
        messageDto.setSentTime(this.sendTime);
        return messageDto;

    }
}
