package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.ChatRoomDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ChatRoomResponse {
    private Boolean success;
    private String message;
    private ChatRoomDto chatRoomDto;
}
