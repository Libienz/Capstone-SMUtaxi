package com.capstone.smutaxi.utils;

import com.capstone.smutaxi.entity.WaitingRoom;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이 클래스는 스프링 부트 애플리케이션이 시작될때 run 메서드가 실행되는데
 * 쓰레드 풀처럼 미리 300개의 ChatRoom을 만들어놓는 클래스
 * ChatRoom1 ~ 100까지는 F_ONLY
 * ChatRoom100 ~ 200까지는 M_ONLY
 * ChatRoom200 ~ 300까지는 ANY
 */
@RequiredArgsConstructor
@Component
public class WaitingRoomInitializer implements ApplicationRunner {
    private final WaitingRoomRepository waitingRoomRepository;

    @Value("${matching.pool-size}")
    private int poolSize;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createWaitingRoomPool();
    }

    private void createWaitingRoomPool() {
        for (int i = 1; i <= poolSize; i++) {
            WaitingRoom waitingRoom = WaitingRoom.createWaitingRoom();
            waitingRoomRepository.save(waitingRoom);
        }
    }
}

