package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.match.MatchCancelRequest;
import com.capstone.smutaxi.dto.requests.match.MatchingRequest;
import com.capstone.smutaxi.dto.responses.match.MatchCancelResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponse;

public interface MatchingService {

    public MatchingResponse handleMatchingRequest(MatchingRequest matchingRequest);

    public MatchCancelResponse cancelMatchRequest(MatchCancelRequest matchCancelRequest);
}
