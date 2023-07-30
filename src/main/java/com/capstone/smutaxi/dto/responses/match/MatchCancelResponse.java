package com.capstone.smutaxi.dto.responses.match;


import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class MatchCancelResponse {
    private Boolean success;
    private String message;
}
