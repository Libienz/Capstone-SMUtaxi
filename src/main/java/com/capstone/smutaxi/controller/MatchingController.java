package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.requests.MatchingRequest;
import com.capstone.smutaxi.dto.responses.MatchingResponseDto;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<MatchingResponseDto> matching(MatchingRequest matchingRequest) {
        MatchingResponseDto matchingResponseDto = matchingService.handleMatchingRequest(matchingRequest);
        return ResponseEntity.ok(matchingResponseDto);
    }

}
