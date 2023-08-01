package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.match.MatchCancelRequest;
import com.capstone.smutaxi.dto.responses.match.MatchCancelResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.entity.WaitingRoom;
import com.capstone.smutaxi.entity.WaitingRoomUser;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.matching.CannotJoinWaitingRoomException;
import com.capstone.smutaxi.dto.requests.match.MatchingRequest;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import com.capstone.smutaxi.repository.WaitingRoomUserRepository;
import com.capstone.smutaxi.service.FcmService;
import com.capstone.smutaxi.utils.Location;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndividualMatchingService implements MatchingService {


    private final WaitingRoomRepository waitingRoomRepository;
    private final WaitingRoomUserRepository waitingRoomUserRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    /**
     * Individual Style Match Handling
     * 미리 만들어져 있는 Waiting Room Pool에 TMA 방식으로 매칭 요청
     * 매칭 완료: 4명 모임 -> 문닫고 온사람이 알리는걸로? 아니면 listener 구현?
     */
    @Transactional
    @Override
    public MatchingResponse handleMatchingRequest(MatchingRequest matchingRequest) {

        //유저 get
        String requestorId = matchingRequest.getEmail();
        User user = userRepository.findById(requestorId).get();
        //유저 위치 정보
        double latitude = matchingRequest.getLatitude();
        double longitude = matchingRequest.getLongitude();
        //fcm deviceToken
        String deviceToken = matchingRequest.getDeviceToken();
        Location userLocation = new Location(latitude, longitude);

        //WaitingRoom list 받아오기
        List<WaitingRoom> waitingRooms = waitingRoomRepository.findAll();
        //들어있는 인원수 내림차순 정렬
        Collections.sort(waitingRooms);

        //모든 웨이팅 룸 돌면서 TMA
        for (WaitingRoom waitingRoom : waitingRooms) {
            boolean canEnter = knockWaitingRoom(user, userLocation, waitingRoom);
            if (canEnter) {
                //참가 정보 생성 및 대기방 입장
                WaitingRoomUser entranceInformation = enterWaitingRoom(user, waitingRoom, deviceToken);
                Long waitingRoomId = waitingRoom.getId();
                Long waitingRoomUserId = entranceInformation.getId();
                if (waitingRoom.getWaiters().size() == 4) {
                    try {
                        processMatchSuccess(waitingRoom);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return ResponseFactory.createMatchingResponse(Boolean.TRUE, null, waitingRoomId, waitingRoomUserId);
            }
        }
        throw new CannotJoinWaitingRoomException(ErrorCode.INTERNAL_SERVER_ERROR); //flow가 여기로 오면 안됨 -> 무조건 waiting Room에 배치되어야 함

    }


    @NotNull
    private WaitingRoomUser enterWaitingRoom(User user, WaitingRoom waitingRoom, String deviceToken) {
        WaitingRoomUser waitingRoomUser = WaitingRoomUser.createWaitingRoomUser(waitingRoom, user, deviceToken);
        waitingRoomUserRepository.save(waitingRoomUser);
        waitingRoom.getWaiters().add(waitingRoomUser);
        return waitingRoomUser;
    }


    private boolean knockWaitingRoom(User user, Location userLocation, WaitingRoom waitingRoom) {

        //빈방이면 들어간다.
        if (waitingRoom.getWaiters().size() == 0) {
            waitingRoom.setLocation(userLocation);
            return true;
        }
        //빈방 아닐 시 대기방의 대표 위치와 요청자의 위치를 비교
        Location waitingRoomLocation = waitingRoom.getLocation();
        double distance = Location.calculateDistance(userLocation, waitingRoomLocation);
        if (distance <= 500) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public MatchCancelResponse cancelMatchRequest(MatchCancelRequest matchCancelRequest) {


        Long waitingRoomId = matchCancelRequest.getWaitingRoomId();
        Long waitingRoomUserId = matchCancelRequest.getWaitingRoomUserId();

        WaitingRoom waitingRoom = waitingRoomRepository.findById(waitingRoomId).get();
        List<WaitingRoomUser> waiters = waitingRoom.getWaiters();
        for (int i = 0; i < waiters.size(); i++) {
            if (waiters.get(i).getId() == waitingRoomUserId) {
                waiters.remove(i);
                break;
            }
        }
        waitingRoomUserRepository.deleteById(waitingRoomUserId);
        return ResponseFactory.createMatchCancelResponse(Boolean.TRUE, null);
    }

    private void processMatchSuccess(WaitingRoom waitingRoom) throws IOException {
        List<WaitingRoomUser> waiters = waitingRoom.getWaiters();
        //create Chat Room & push chatRoomId
        for (WaitingRoomUser wru : waiters) {
            String targetToken = wru.getDeviceToken();
            fcmService.sendMessageTo(targetToken, "matchSuccess", "chatRoom: 1");
        }
        waitingRoomRepository.delete(waitingRoom);

    }
}
