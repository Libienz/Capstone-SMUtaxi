package com.capstone.smutaxi.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchingRequest {
    private String email;
    private double latitude;
    private double longitude;

}
