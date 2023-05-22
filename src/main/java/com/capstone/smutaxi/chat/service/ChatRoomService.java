package com.capstone.smutaxi.chat.service;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(rollbackFor = Exception.class)
    public ChatRoom joinChatRoom(Long id){
        Optional<ChatRoom> byId = chatRoomRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    @Transactional
    public ChatRoom createChatRoom(String name){
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getRoomList() {
        return chatRoomRepository.findAll();
    }
}
