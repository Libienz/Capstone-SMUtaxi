package com.capstone.smutaxi.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
public class MessageDto {
    private Long messageId;
    private String senderId;
    private String senderName;
    private String message;
    private String senderProfileImageUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime sentTime;
    private Boolean isSystem;
}
