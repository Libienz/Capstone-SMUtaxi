package com.capstone.smutaxi.chat.service;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.ChatRoomUser;
import com.capstone.smutaxi.chat.repository.ChatRoomRepository;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public ChatRoom joinChatRoom(Long id){
        Optional<ChatRoom> byId = chatRoomRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    @Transactional
    public List<ChatRoom> getChatRoomsByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).get();
        List<ChatRoom> chatRooms = new ArrayList<>();

        if (user != null) {
            List<ChatRoomUser> chatRoomUsers = user.getChatRoomUsers();

            for (ChatRoomUser chatRoomUser : chatRoomUsers) {
                chatRooms.add(chatRoomUser.getChatRoom());
            }
        }

        return chatRooms;
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
