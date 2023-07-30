package com.capstone.smutaxi.dto.responses.match;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MatchingResponse {
    private Boolean success;
    private String message;
    private Long waitingRoomId;
    private Long waitingRoomUserId;
}
