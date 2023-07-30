package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.utils.Location;
import lombok.*;


import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {
    private Long chatParticipantId;
    private Long chatRoomId;
    private String chatRoomName;
    private Location chatRoomLocation;
    private List<MessageDto> messageList;

    @Getter
    @Setter
    public static class MessageDto{

        private Long id;
        private String senderEmail;
        private String senderName;
        private String sendTime;
        private String message;
    }
}
