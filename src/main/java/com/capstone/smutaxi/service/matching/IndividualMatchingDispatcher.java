package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.GenderRestriction;
import com.capstone.smutaxi.chat.repository.ChatRoomRepository;
import com.capstone.smutaxi.chat.service.ChatRoomUserService;
import com.capstone.smutaxi.dto.MatchingRequestDto;
import com.capstone.smutaxi.entity.Location;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    private ChatRoomRepository chatRoomRepository;
    private UserRepository userRepository;
    private ChatRoomUserService chatRoomUserService;

    public IndividualMatchingDispatcher(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
    }


    @Override
    public Long handleMatchingRequest(String userEmail, MatchingRequestDto matchingRequestDto) {

        GenderRestriction userGenderRestriction = matchingRequestDto.getGenderRestriction();
        //채팅방 리스트 받아온다.
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        //성별 제약조건이 user의 요청과 같은 chatRoom들을 filtering
        List<ChatRoom> genderfilteredChatRooms = chatRooms.stream()
                .filter(chatRoom -> chatRoom.getGenderRestriction().equals(userGenderRestriction))
                .collect(Collectors.toList());

        //채팅방 인원 수 내림차순 정렬: chatRoom Comparable
        Collections.sort(genderfilteredChatRooms);

        //유저 위치정보 생성
        double latitude = matchingRequestDto.getLatitude();
        double longitude = matchingRequestDto.getLongitude();
        Location userLocation = new Location(latitude, longitude);

        for (ChatRoom chatRoom : genderfilteredChatRooms) {

            Location chatRoomLocation = chatRoom.getLocation();
            //채팅방 대표위치와 나의 위치 비교
            double distance = Location.calculateDistance(userLocation, chatRoomLocation); //채팅방 대표위치와 유저 위치간의 거리 (단위:m)
            if (distance <= 500) {
                // 성별 조건도 맞고 위치 조건도 맞으면 채팅방에 유저 추가

                // 채팅방 정보 update(initLocation): 빈 채팅방과 매칭된 경우는 chatRoom의 대표 위치를 초기화 한다.
                if (chatRoom.getLocation() == null) {
                    Location copiedLocation = new Location(latitude, longitude); //깊은 복사
                    chatRoomRepository.initLocation(chatRoom.getId(), copiedLocation);
                }
                // 채팅방 정보 update(addUser): 채팅방에 참가중인 유저를 추가한다.
                //chatRoomRepository.addUser(chatRoom.getId(), userEmail);
                chatRoomUserService.addChatRoomUser(userEmail,chatRoom.getId());

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
