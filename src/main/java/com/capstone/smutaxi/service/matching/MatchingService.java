package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.match.MatchingRequest;
import com.capstone.smutaxi.dto.responses.match.MatchingResponseDto;

public interface MatchingService {

    public MatchingResponseDto handleMatchingRequest(MatchingRequest matchingRequest);
}
