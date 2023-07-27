package com.capstone.smutaxi.dto.requests;

import lombok.Getter;

@Getter
public class MatchingRequest {
    private String email;
    private double latitude;
    private double longitude;

}
