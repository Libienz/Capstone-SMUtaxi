package com.capstone.smutaxi.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MatchingResponseDto {
    private Boolean success;
    private String message;
    private Long waitingRoomId;
}
