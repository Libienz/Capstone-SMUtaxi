package com.capstone.smutaxi.service;

import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.ChatParticipantRepository;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatRoom joinChatRoom(Long id) {
        Optional<ChatRoom> byId = chatRoomRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    public List<ChatRoom> getUserJoinedChatRooms(String userEmail) {
        List<ChatParticipant> participantInfo = chatParticipantRepository.findByUserEmail(userEmail);
        List<ChatRoom> chatRooms = new ArrayList<>();

        for (ChatParticipant pi : participantInfo) {
            chatRooms.add(pi.getChatRoom());
        }
        return chatRooms;
    }

    /* 채팅방에 유저를 추가 : ChatParticipant를 하나 더 생성하면 끝 */
    @Transactional
    public void addUserToChatRoom(Long chatRoomId, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setChatRoom(chatRoom);
        chatParticipant.setUser(user);
        chatParticipantRepository.save(chatParticipant);
    }

    public int getParticipantCount(Long chatRoomId) {
        return chatParticipantRepository.countByChatRoomId(chatRoomId);
    }

    @Transactional
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public List<ChatRoom> getRoomList() {
        return chatRoomRepository.findAll();
    }
}
