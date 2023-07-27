package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.ChatRoomDto;
import com.capstone.smutaxi.dto.responses.ChatRoomResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.user.UserNotFoundException;
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

    //유저가 참여하고있는 ChatRoom들의 Response 반환
    public List<ChatRoomResponse> getUserJoinedChatRooms(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        List<ChatRoomResponse> chatRoomResponseList = new ArrayList<>();

        List<ChatParticipant> chatParticipantList = user.getChatParticipantList();
        for (ChatParticipant chatParticipant : chatParticipantList) {
            ChatRoom chatRoom = chatParticipant.getChatRoom();
            ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                    .chatRoomId(chatRoom.getId())
                    .chatRoomLocation(chatRoom.getChatRoomLocation())
                    .chatRoomName(chatRoom.getChatRoomName())
                    .messageList(chatRoom.getMessageList())
                    .build();

            //response Dto 생성 -> {Boolean success, String message, ChatRoomDto chatRoomDto}
            chatRoomResponseList.add(ResponseFactory.createChatRoomResponse(true, null, chatRoomDto));
        }

        return chatRoomResponseList;
    }

    /* 채팅방에 유저를 추가 : ChatParticipant를 하나 더 생성하면 끝 */
    @Transactional
    public void addUserToChatRoom(Long chatRoomId, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setChatRoom(chatRoom);
        chatParticipant.setUser(user);
        chatParticipantRepository.save(chatParticipant);
    }
    //중간테이블인 chatParticipant에서 특정 chatRoomId를 가진 엔티티의 개수를 반환
    public int getParticipantCount(Long chatRoomId) {
        return chatParticipantRepository.countByChatRoomId(chatRoomId);
    }

    //ChatRoom 생성
    @Transactional
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    //채팅방 전부 바놘
    public List<ChatRoom> getRoomList() {
        return chatRoomRepository.findAll();
    }
}
