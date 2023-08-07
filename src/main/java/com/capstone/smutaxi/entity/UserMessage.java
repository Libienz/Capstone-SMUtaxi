package com.capstone.smutaxi.entity;

import com.capstone.smutaxi.dto.MessageDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("U")
@Getter @Setter
public class UserMessage extends Message{
    private String senderEmail;
    private String senderProfileImageUrl;

    @Override
    public MessageDto toMessageDto() {
        MessageDto messageDto = super.toMessageDto();
        messageDto.setSenderId(this.senderEmail);
        messageDto.setSenderProfileImageUrl(this.senderProfileImageUrl);
        return messageDto;
    }
}
