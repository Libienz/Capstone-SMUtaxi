package com.capstone.smutaxi.service.matching;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.repository.ChatRoomRepository;
import com.capstone.smutaxi.dto.MatchingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Autowired
    public IndividualMatchingDispatcher(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public void handleMatchingRequest(String userEmail, MatchingRequestDto matchingRequestDto) {

        //채팅방 리스트 받아온다.
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        for (ChatRoom chatRoom : chatRooms) {
            //현재 보고 있는 chatRoom이 요청자가 입장하기에 적절한 지 따진다.

            //1. 성별 제약 확인

            //2. 위치 맞는지 확인

            //현재 chatRooms에는 아무도 들어있지 않은 빈채팅방도 있기에 사람이 들어있는 채팅방 중 조건에 맞는 채팅방이 없더라도 채팅방에 참여하지 못할 일은 없다.

        }
    }
}
