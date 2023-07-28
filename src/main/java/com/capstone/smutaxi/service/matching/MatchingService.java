package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.MatchingRequest;
import com.capstone.smutaxi.dto.responses.MatchingResponseDto;

public interface MatchingService {

    public MatchingResponseDto handleMatchingRequest(MatchingRequest matchingRequest);
}
