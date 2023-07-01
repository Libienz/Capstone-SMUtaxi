package com.capstone.smutaxi.chat.service;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.ChatRoomUser;
import com.capstone.smutaxi.chat.repository.ChatRoomRepository;
import com.capstone.smutaxi.chat.repository.ChatRoomUserRepository;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomUserService(ChatRoomUserRepository chatRoomUserRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.chatRoomUserRepository = chatRoomUserRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }


    public void addChatRoomUser(String userEmail, Long chatRoomId) {
        User user = userRepository.findByEmail(userEmail).get();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();

        ChatRoomUser chatRoomUser = new ChatRoomUser();

        user.addChatRoomUser(chatRoomUser);
        chatRoom.addChatRoomUser(chatRoomUser);

        chatRoomUserRepository.save(chatRoomUser);
    }


}
