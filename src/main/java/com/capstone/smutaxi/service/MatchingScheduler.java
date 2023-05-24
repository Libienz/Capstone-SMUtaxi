package com.capstone.smutaxi.service;

import com.capstone.smutaxi.service.matching.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchingScheduler {

    MatchingService matchingService;

    @Autowired
    public MatchingScheduler(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @Scheduled(fixedDelay = 3000) // 3초마다 실행
    public void matchRequests() {
        // 매칭 요청 조회 및 매칭 로직 실행
        matchingService.handleMatchingRequest();
    }


}
