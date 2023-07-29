package com.capstone.smutaxi.config;

import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import com.capstone.smutaxi.repository.WaitingRoomUserRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.matching.IndividualMatchingService;
import com.capstone.smutaxi.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MatchingConfig {

    private final UserRepository userRepository;
    private final WaitingRoomRepository waitingRoomRepository;
    private final WaitingRoomUserRepository waitingRoomUserRepository;
    @Bean
    public MatchingService matchingDispatcher() {
        //한명이 모일때마다 채팅방으로 바로바로 넣는 IndividualDispatcher (빠른 응답 유저 경험 좋음 but 빈채팅방도 관리, 채팅방 부족 현상등이 발생가능)
        //4명을 모아서 한번에 채팅방으로 쏘는 BatchDispatcher (유저 대기시간 길어질 수 있음 but 간단하고 로직이 꼬일일이 많이 없음)
        return new IndividualMatchingService(waitingRoomRepository, waitingRoomUserRepository, userRepository );
    }


}
