package com.capstone.smutaxi.service;

import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    public void matchRequests() {
        //DB에서 matching_requests table 조회

        //만들어진지 가장 오래된 요청을 가져온다.

        //나머지 요청들과의 거리계산을 Haversine 공식이나 Vincenty 공식을 이용하여 진행

        //조건에 맞으면 한 그룹으로 묶는다.

        //4명이 모이면 채팅방을 만들고 채팅방으로 쏜다.
    }

}
