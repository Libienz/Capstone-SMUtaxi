package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.MatchingRequest;

public interface MatchingDispatcher {

    public Long handleMatchingRequest(String userEmail, MatchingRequest matchingRequest);
}
