package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.dto.requests.MatchingRequest;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.utils.Location;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class IndividualMatchingDispatcher implements MatchingDispatcher{
    /**
     * Individual Style Dispatcher
     * chatRoomPool을 만들어 놓는다. pool_size는 yml파일에 ..
     * chatRoom은 들어있는 인원수 내림차순으로 sorting?
     * matching Request가 생성될 때마다 '채팅방 리스트'를 살펴보고 들어갈 수 있는 곳이 있는 지 체크한다.
     * 즉 꼭 4명이 아니더라도 들어갈 수 있는 채팅방이 있으면 바로바로 들어간다.
     * pros : 사용자 경험 개선 (기다리는 시간이 상대적으로 짧다고 느낄 수 있음)
     * cons : 잦은 DB 요청
     */

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

//    private ChatRoomUserService chatRoomUserService;




    @Override
    public Long handleMatchingRequest(String userEmail, MatchingRequest matchingRequest) {

        //채팅방 리스트 받아온다.
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        //유저 위치정보
        double latitude = matchingRequest.getLatitude();
        double longitude = matchingRequest.getLongitude();
        Location userLocation = new Location(latitude, longitude);

        for (ChatRoom chatRoom : chatRooms) {

            //채팅방 인원이 4명보다 많다면 패스
            Long chatRoomId = chatRoom.getId();
            int participantCount = chatRoomService.getParticipantCount(chatRoomId);
            if (participantCount >= 4) {
                continue;
            }
            //채팅방 인원이 4명보다 채팅방의 대표 위치를 받아온다.
            Location chatRoomLocation = chatRoom.getChatRoomLocation();
            //채팅방 대표위치와 나의 위치 비교
            double distance = Location.calculateDistance(userLocation, chatRoomLocation); //채팅방 대표위치와 유저 위치간의 거리 (단위:m)
            if (distance <= 500) { //위치가 500m이내라면 매칭 가능
                // 채팅방 정보 update(initLocation): 빈 채팅방과 매칭된 경우는 chatRoom의 대표 위치를 초기화 한다.
                if (chatRoom.getChatRoomLocation() == null) {
                    Location copiedLocation = new Location(latitude, longitude); //깊은 복사
                    chatRoomRepository.initLocation(chatRoom.getId(), copiedLocation);
                }
                // 채팅방 정보 update(addUser): 채팅방에 참가중인 유저를 추가한다.
                //chatRoomRepository.addUser(chatRoom.getId(), userEmail);
                chatRoomService.addUserToChatRoom(chatRoom.getId(), userEmail);

                //채팅방 id 반환
                return chatRoom.getId();

            }

        }
        // 현재 chatRooms에는 아무도 들어있지 않은 빈채팅방도 있기에 사람이 들어있는 채팅방 중 조건에 맞는 채팅방이 없더라도 채팅방에 참여하지 못할 일은 없다.
        // iteration 끝나기 전에 무조건 참여하게 되어있음
        // 다만 pool관리 철저히 필요
        return null;
    }
}
