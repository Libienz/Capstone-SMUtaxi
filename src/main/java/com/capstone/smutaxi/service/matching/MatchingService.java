package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.dto.requests.match.MatchCancelRequest;
import com.capstone.smutaxi.dto.responses.match.MatchCancelResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.entity.WaitingRoom;
import com.capstone.smutaxi.entity.WaitingRoomUser;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.matching.CannotJoinWaitingRoomException;
import com.capstone.smutaxi.dto.requests.match.MatchingRequest;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.repository.WaitingRoomRepository;
import com.capstone.smutaxi.repository.WaitingRoomUserRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.FcmService;
import com.capstone.smutaxi.utils.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class MatchingService {


    private final WaitingRoomRepository waitingRoomRepository;
    private final WaitingRoomUserRepository waitingRoomUserRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final ChatRoomService chatRoomService;

    /**
     * Individual Style Match Handling
     * 미리 만들어져 있는 Waiting Room Pool에 TMA 방식으로 매칭 요청
     * 매칭 완료: 4명 모임 -> 문닫고 온사람이 알리는걸로? 아니면 listener 구현?
     */
    @Transactional
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
                        for (WaitingRoomUser wru : waitingRoom.getWaiters()) {
                            fcmService.sendMessageTo(wru.getDeviceToken(), "상명대행 택시팟 매칭 성공!", "매칭된 채팅방으로 얼른 이동하세요!");
                        }
                        waitingRoomRepository.delete(waitingRoom);
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
        if (waitingRoom.getWaiters().size() == 0) {
            waitingRoomRepository.deleteById(waitingRoom.getId());
        }
        return ResponseFactory.createMatchCancelResponse(Boolean.TRUE, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Long processMatchSuccess(WaitingRoom waitingRoom) throws IOException {
        List<WaitingRoomUser> waiters = waitingRoom.getWaiters();
        //create Chat Room
        String chatRoomName = generateChatRoomName(waiters);
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomName);
        //add waiters to chatRoom
        for (WaitingRoomUser wru : waiters) {
            chatRoomService.addUserToChatRoom(chatRoom.getId(), wru.getUser().getEmail());
        }
        return chatRoom.getId();
    }

    private String generateChatRoomName(List<WaitingRoomUser> waiters) {
        StringBuilder chatRoomNameBuilder = new StringBuilder();
        for (int i = 0; i < waiters.size(); i++) {
            chatRoomNameBuilder.append(waiters.get(i).getUser().getName());
            if (i < waiters.size() - 1) {
                chatRoomNameBuilder.append(", ");
            }
        }

        String chatRoomName = chatRoomNameBuilder.toString();
        if (chatRoomName.length() > 12) {
            chatRoomName = chatRoomName.substring(0, 12) + "...";
        }
        return chatRoomName;
    }

}
