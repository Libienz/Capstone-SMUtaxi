package com.capstone.smutaxi.service;

import com.capstone.smutaxi.dto.MessageDto;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.UserJoinedChatRoomDto;
import com.capstone.smutaxi.dto.responses.chat.ChatRoomMessageResponse;
import com.capstone.smutaxi.dto.responses.chat.UpdateChatParticipantResponse;
import com.capstone.smutaxi.dto.responses.chat.UserJoinedChatRoomResponse;
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

            //안읽은 메시지 카운트 (참여 정보에 채팅방을 나간 시점이 기록되어 있다)
            int nonReadMessageCount = countNonReadMessage(chatParticipant, messageList);
            //가장 마지막 메시지 받아오기
            Message lastSentMessage = getLastSentMessage(messageList);
            if (lastSentMessage != null) {
                lastMessage = lastSentMessage.getMessage();
                lastSentTime = lastSentMessage.getSendTime();
            }

            //현재 루프에서 보고있는 채팅방(유저가 참여하고 있는 채팅방 중 하나)에 대한 정보를 dto로 변환
            List<ChatParticipant> chatRoomParticipant = chatRoom.getChatRoomParticipant();
            List<UserDto> participantsList = new ArrayList<>();
            for (ChatParticipant participant : chatRoomParticipant) {
                participantsList.add(participant.getUser().userToUserDto());
            }

            UserJoinedChatRoomDto userJoinedChatRoomDto = UserJoinedChatRoomDto.builder()
                    .chatRoomId(chatRoom.getId())
                    .chatRoomName(chatRoom.getChatRoomName())
                    .lastMessage(lastMessage)
                    .nonReadMessageCount(nonReadMessageCount)
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

        ChatParticipant chatParticipant = ChatParticipant.createChatParticipant(chatRoom, user, LocalDateTime.now());
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
        Optional<ChatParticipant> findChatParticipant = chatParticipantRepository.findQuerydslById(chatParticipantId);
        if(findChatParticipant.isEmpty()){
            throw new ChatParticipantNotFoundException(ErrorCode.CHATPARTICIPANT_NOT_FOUND);
        }

        findChatParticipant.get().remove();

        // ChatParticipant 엔티티를 삭제
        chatParticipantRepository.delete(findChatParticipant.get());

    }
    public ChatRoomMessageResponse getChatRoomMessages(String userId, Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(ErrorCode.CHATROOM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        Long chatParticipantId = null;

        List<ChatParticipant> chatRoomParticipant = chatRoom.getChatRoomParticipant();
        for (ChatParticipant chatParticipant : chatRoomParticipant) {
            if(chatParticipant.getUser().equals(user)){
                chatParticipantId=chatParticipant.getId();
            }
        }
        List<MessageDto> messageDtoList = new ArrayList<>();
        List<Message> messageList = chatRoom.getMessageList();
        for (Message message : messageList) {
            MessageDto messageDto = message.toMessageDto();
            messageDtoList.add(messageDto);
        }

        ChatRoomMessageResponse chatRoomMessageResponse = ResponseFactory.createChatRoomMessageResponse(true, null, chatParticipantId, messageDtoList);

        return chatRoomMessageResponse;
    }

    //ChatRoom 생성
    @Transactional
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    @Transactional
    public UpdateChatParticipantResponse updateChatParticipant(Long chatParticipnatId) {
        ChatParticipant chatParticipant = chatParticipantRepository.findById(chatParticipnatId)
                .orElseThrow(() -> new ChatParticipantNotFoundException(ErrorCode.CHATPARTICIPANT_NOT_FOUND));
        chatParticipant.setLastLeaveTime(LocalDateTime.now());
        return ResponseFactory.createUpdateChatParticipantResponse(Boolean.TRUE, null);
    }

    // List<Message>에서 가장 최근의 메시지를 찾아 반환하는 메서드
    private Message getLastSentMessage(List<Message> messageList) {
        // 메시지가 없는 경우 null 반환
        if (messageList == null || messageList.isEmpty()) {
            return null;
        }
        // 초기값으로 첫 번째 메시지를 최근 메시지로 설정
        Message latestMessage = messageList.get(0);

        // 모든 메시지를 순회하며 최근 메시지를 찾음
        for (Message message : messageList) {
            Long curId = message.getId();
            if (latestMessage.getId() < curId) {
                latestMessage = message;
            }
        }
        return latestMessage;
    }

    //entrnace information(chatParticipant)로 안읽은 메시지 카운트
    private static int countNonReadMessage(ChatParticipant chatParticipant, List<Message> messageList) {
        int nonReadMessageCount = 0;
        for (Message message : messageList) {
            LocalDateTime sendTime = message.getSendTime();
            LocalDateTime lastLeaveTime = chatParticipant.getLastLeaveTime();
            if (lastLeaveTime.isBefore(sendTime)) {
                nonReadMessageCount++;
            }
        }
        return nonReadMessageCount;
    }

}
