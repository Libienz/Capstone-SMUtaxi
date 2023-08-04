package com.capstone.smutaxi.dto.responses.chat;

import com.capstone.smutaxi.dto.MessageDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMessageResponse {
    private Boolean success;
    private String message;
    private Long chatParticipantId;
    private List<MessageDto> messageDtoList;
}
