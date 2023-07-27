package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.WaitingRoom;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.dto.requests.MatchingRequest;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.utils.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndividualMatchingService implements MatchingService {


    private final WaitingRoomRepository waitingRoomRepository;

    /**
     * Individual Style Match Handling
     * 미리 만들어져 있는 Waiting Room Pool에 TMA 방식으로 매칭 요청
     */
    @Transactional
    @Override
    public Long handleMatchingRequest(MatchingRequest matchingRequest) {

        //유저 정보 초기화
        String requestorId = matchingRequest.getEmail();
        double latitude = matchingRequest.getLatitude();
        double longitude = matchingRequest.getLongitude();
        Location userLocation = new Location(latitude, longitude);

        //WaitingRoom list 받아오기 (들어있는 인원수 내림차순 정렬되어 있음)
        List<WaitingRoom> waitingRooms = waitingRoomRepository.findAll();

        //모든 웨이팅 룸 돌면서 TMA
        for (WaitingRoom waitingRoom : waitingRooms) {

            //빈방이면 들어간다.
            if (waitingRoom.getWaiters().size() == 0) {
                waitingRoom.setLocation(userLocation);
                waitingRoom.getWaiters().add(requestorId);
                return waitingRoom.getId();
            }
            Location waitingRoomLocation = waitingRoom.getLocation();

            //대기방의 대표 위치와 요청자의 위치를 비교, 성공 여부 핸들링
            double distance = Location.calculateDistance(userLocation, waitingRoomLocation);
            if (distance <= 500) {
                waitingRoom.getWaiters().add(requestorId);
                return waitingRoom.getId();
            }
        }
        return null;
    }
}
