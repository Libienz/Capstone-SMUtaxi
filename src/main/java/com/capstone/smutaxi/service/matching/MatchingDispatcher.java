package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.MatchingRequestDto;

public interface MatchingDispatcher {

    public Long handleMatchingRequest(String userEmail, MatchingRequestDto matchingRequestDto);
}
