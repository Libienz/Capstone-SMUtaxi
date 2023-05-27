package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.MatchingRequestDto;

public interface MatchingDispatcher {

    public void handleMatchingRequest(String userEmail, MatchingRequestDto matchingRequestDto);
}
