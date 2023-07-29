package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.requests.match.MatchCancelRequest;
import com.capstone.smutaxi.dto.requests.match.MatchingRequest;
import com.capstone.smutaxi.dto.responses.match.MatchCancelResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponse;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/match")
@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;
    private final UserRepository userRepository;

    @ResponseBody
    @PostMapping("/request")
    public ResponseEntity<MatchingResponse> matching(MatchingRequest matchingRequest) {
        MatchingResponse matchingResponse = matchingService.handleMatchingRequest(matchingRequest);
        return ResponseEntity.ok(matchingResponse);
    }

    @ResponseBody
    @PostMapping("/cancel-request")
    public ResponseEntity<MatchCancelResponse> matching(MatchCancelRequest matchCancelRequest) {
        MatchCancelResponse matchCancelResponse = matchingService.cancelMatchRequest(matchCancelRequest);
        return ResponseEntity.ok(matchCancelResponse);
    }


}
