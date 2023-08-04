package com.capstone.smutaxi.dto.requests.match;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchingRequest {
    private String email;
    private String deviceToken;
    private double latitude;
    private double longitude;

}
