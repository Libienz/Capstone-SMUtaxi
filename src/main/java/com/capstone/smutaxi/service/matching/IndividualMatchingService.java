package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.responses.MatchingResponseDto;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.entity.WaitingRoom;
import com.capstone.smutaxi.entity.WaitingRoomUser;
import com.capstone.smutaxi.exception.BusinessException;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.matching.CannotJoinWaitingRoomException;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.dto.requests.MatchingRequest;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.utils.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndividualMatchingService implements MatchingService {


    private final WaitingRoomRepository waitingRoomRepository;
    private final UserRepository userRepository;

    /**
     * Individual Style Match Handling
     * 미리 만들어져 있는 Waiting Room Pool에 TMA 방식으로 매칭 요청
     * 매칭 완료: 4명 모임 -> 문닫고 온사람이 알리는걸로? 아니면 listener 구현?
     */
    @Transactional
    @Override
    public MatchingResponseDto handleMatchingRequest(MatchingRequest matchingRequest) {

        //유저 get
        String requestorId = matchingRequest.getEmail();
        User user = userRepository.findByEmail(requestorId).get();
        //유저 위치 정보
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
                waitingRoom.getWaiters().add(WaitingRoomUser.createWaitingRoomUser(waitingRoom,user));
                Long waitingRoomId = waitingRoom.getId();
                return ResponseFactory.createMatchingResponse(Boolean.TRUE, null, waitingRoomId);
            }
            Location waitingRoomLocation = waitingRoom.getLocation();

            //대기방의 대표 위치와 요청자의 위치를 비교, 성공 여부 핸들링
            double distance = Location.calculateDistance(userLocation, waitingRoomLocation);
            if (distance <= 500) {
                waitingRoom.getWaiters().add(WaitingRoomUser.createWaitingRoomUser(waitingRoom,user));
                Long waitingRoomId = waitingRoom.getId();
                return ResponseFactory.createMatchingResponse(Boolean.TRUE, null, waitingRoomId);
            }
        }
        throw new CannotJoinWaitingRoomException(ErrorCode.INTERNAL_SERVER_ERROR); //flow가 여기로 오면 안됨 -> 무조건 waiting Room에 배치되어야 함

    }
}
