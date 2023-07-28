package com.capstone.smutaxi.dto.requests.match;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchCancelRequest {
    private String email;
    private Long waitingRoomId;
}
