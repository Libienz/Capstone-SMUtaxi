package com.capstone.smutaxi.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MsgService {
    private final ObjectMapper objectMapper;
    private Map<String, MsgRoom> chatRooms; //서버에 생성된 모든 채팅방 정보를 모아둔 구조체

    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    public List<MsgRoom> findAllRoom(){
        return new ArrayList<>(chatRooms.values());
    }

    public MsgRoom findRoomById(String roomId){
        return chatRooms.get(roomId);
    }

    public MsgRoom createRoom(String name){
        String randomId = UUID.randomUUID().toString();
        MsgRoom msgRoom = MsgRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId,msgRoom);
        return msgRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message){
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }
}
