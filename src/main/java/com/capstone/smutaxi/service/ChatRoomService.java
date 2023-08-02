package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.UserJoinedChatRoomDto;
import com.capstone.smutaxi.dto.responses.UserJoinedChatRoomResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.exception.ChatParticipantDuplicateException;
import com.capstone.smutaxi.exception.ChatParticipantNotFoundException;
import com.capstone.smutaxi.exception.ChatRoomNotFoundException;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.user.UserNotFoundException;
import com.capstone.smutaxi.repository.ChatParticipantRepository;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public UserJoinedChatRoomResponse getUserJoinedChatRooms(String userEmail) {

        User user = userRepository.findById(userEmail)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        List<UserJoinedChatRoomDto> userJoinedChatRoomDtoList = new ArrayList<>();

        List<ChatParticipant> chatParticipantList = user.getChatParticipantList();
        for (ChatParticipant chatParticipant : chatParticipantList) {
            Long chatParticipantId = chatParticipant.getId();

            ChatRoom chatRoom = chatParticipant.getChatRoom();

            List<Message> messageList = chatParticipant.getChatRoom().getMessageList();

            String lastMessage = null;
            LocalDateTime lastSentTime =null;
            // messageList에서 가장 큰 id 값을 찾기
            Optional<Long> maxId = messageList.stream()
                    .map(Message::getId)
                    .max(Long::compareTo);

            if (maxId.isPresent()) {
                Long largestId = maxId.get();

                // 가장 큰 id를 가진 Message 객체를 찾습니다.
                Optional<Message> largestMessage = messageList.stream()
                        .filter(message -> message.getId().equals(largestId))
                        .findFirst();

                if (largestMessage.isPresent()) {
                    Message messageWithLargestId = largestMessage.get();
                    lastMessage = messageWithLargestId.getMessage();
                    lastSentTime = messageWithLargestId.getSendTime();

                    System.out.println("가장 큰 id 값: " + largestId);
                    System.out.println("가장 큰 id를 가진 Message의 message: " + lastMessage);
                    System.out.println("가장 큰 id를 가진 Message의 sendTime: " + lastSentTime);
                } else {
                    System.out.println("해당하는 가장 큰 id를 가진 Message를 찾을 수 없습니다.");
                }
            } else {
                System.out.println("messageList가 비어있습니다.");
            }

            List<ChatParticipant> chatRoomParticipant = chatRoom.getChatRoomParticipant();
            List<UserDto> participantsList = new ArrayList<>();
            for (ChatParticipant participant : chatRoomParticipant) {
                participantsList.add(participant.getUser().userToUserDto());
            }

            UserJoinedChatRoomDto userJoinedChatRoomDto = UserJoinedChatRoomDto.builder()
                    .chatRoomId(chatRoom.getId())
                    .chatRoomName(chatRoom.getChatRoomName())
                    .lastMessage(lastMessage)
                    .lastSentTime(lastSentTime)
                    .participants(participantsList)
                    .build();

            userJoinedChatRoomDtoList.add(userJoinedChatRoomDto);
        }
            //response Dto 생성 -> {Boolean success, String message, ChatRoomDto chatRoomDto}
        UserJoinedChatRoomResponse chatRoomResponse = ResponseFactory.createChatRoomResponse(true, null, userJoinedChatRoomDtoList);

        return chatRoomResponse;
    }

    //채팅방에 유저를 추가 = ChatParticipant 생성
    @Transactional
    public Long addUserToChatRoom(Long chatRoomId, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(ErrorCode.CHATROOM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 이미 있는 ChatParticipant인지 검증
        Optional<ChatParticipant> findChatParticipant = chatParticipantRepository.findByUserEmailAndChatRoomId(userId, chatRoomId);
        if(findChatParticipant.isPresent()){
            throw new ChatParticipantDuplicateException(ErrorCode.CHATPARTICIPANT_DUPLICATION);
        }

        ChatParticipant chatParticipant = new ChatParticipant();

        //연관관계 메서드
        chatParticipant.setChatRoomAndUser(chatRoom,user);

        ChatParticipant save = chatParticipantRepository.save(chatParticipant);
        return save.getId();
    }
    //중간테이블인 chatParticipant에서 특정 chatRoomId를 가진 엔티티의 개수를 반환
    public int getParticipantCount(Long chatRoomId) {
        return chatParticipantRepository.countByChatRoomId(chatRoomId);
    }

    //ChatParticipant삭제 메서드 (삭제될때 유저와 chatRoom의 list는 조회할떄 없어지고 chatRoom의 Message는 남아있다)
    @Transactional
    public void leaveChatParticipant(Long chatParticipantId) {
        //ChatParticipant가 있는지 검증
        Optional<ChatParticipant> findChatParticipant = chatParticipantRepository.findById(chatParticipantId);
        if(findChatParticipant.isEmpty()){
            throw new ChatParticipantNotFoundException(ErrorCode.CHATPARTICIPANT_NOT_FOUND);
        }
        // ChatParticipant 엔티티를 삭제
        chatParticipantRepository.delete(findChatParticipant.get());

    }

    //ChatRoom 생성
    @Transactional
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

}
