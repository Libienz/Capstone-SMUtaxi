package com.capstone.smutaxi.entity;

import com.capstone.smutaxi.dto.MessageDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
@Getter @Setter
public class SystemMessage extends Message{
    private Boolean isSystem;

    @Override
    public MessageDto toMessageDto() {
        MessageDto messageDto = super.toMessageDto();
        messageDto.setIsSystem(this.isSystem);
        return messageDto;
    }
}
