package com.capstone.smutaxi.dto.responses.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveChatParticipantResponse {
    private Boolean success;
    private String message;
}
