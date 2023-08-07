package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.dto.responses.chat.ChatRoomMessageResponse;
import com.capstone.smutaxi.dto.responses.chat.UpdateChatParticipantResponse;
import com.capstone.smutaxi.dto.responses.chat.UserJoinedChatRoomResponse;
import com.capstone.smutaxi.dto.responses.chat.LeaveChatParticipantResponse;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.entity.SystemMessage;
import com.capstone.smutaxi.entity.UserMessage;
import com.capstone.smutaxi.repository.MessageRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void chat(UserMessage message) {
        Message sendUserMessage = messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getChatRoom().getId(), sendUserMessage);
    }

    @MessageMapping("/exit")
    public void chat(SystemMessage systemMessage) {
        systemMessage.setMessage(systemMessage.getSenderName()+"님이 나갔습니다.");
        systemMessage.setIsSystem(true);

        Message sendExitMessage = messageService.saveMessage(systemMessage);
        messagingTemplate.convertAndSend("/sub/channel/" + systemMessage.getChatRoom().getId(), sendExitMessage);
    }

    //채팅방 생성 API
    @PostMapping("/create-chatRoom")
    public ResponseEntity<String> createChatRoom(@RequestParam String chatRoomName) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomName);
        return ResponseEntity.ok("create chatRoom successfully. chatRoomId: " + chatRoom.getId());
    }

    //채팅방에 유저추가 API
    @PostMapping("/add-user")
    public ResponseEntity<String> addChatRoomUser(@RequestParam String userEmail, @RequestParam Long chatRoomId) {
        Long chatParticipantId = chatRoomService.addUserToChatRoom(chatRoomId, userEmail);
        return ResponseEntity.ok("User added to the chat room successfully. chatParticipantId: " + chatParticipantId);
    }

    @PostMapping("/leave")
    public ResponseEntity<LeaveChatParticipantResponse> leaveChatParticipant(@RequestParam Long chatParticipantId) {
        chatRoomService.leaveChatParticipant(chatParticipantId);
        LeaveChatParticipantResponse leaveChatParticipantResponse = LeaveChatParticipantResponse.builder().success(true).message("User leave to the chat room successfully.").build();
        return ResponseEntity.ok(leaveChatParticipantResponse);
    }

    //유저가 참가한 ChatRoom의 이름과 Id 반환 API
    @GetMapping("/user/chatRooms")
    public ResponseEntity<UserJoinedChatRoomResponse> getUserChatRooms(@RequestParam String email) {
        UserJoinedChatRoomResponse userJoinedChatRooms = chatRoomService.getUserJoinedChatRooms(email);
        return ResponseEntity.ok(userJoinedChatRooms);
    }

    @GetMapping("/chatRoom/messages")
    public ResponseEntity<ChatRoomMessageResponse> getChatRoomMessages(@RequestParam String userEmail, @RequestParam Long chatRoomId) {
        ChatRoomMessageResponse chatRoomMessages = chatRoomService.getChatRoomMessages(userEmail, chatRoomId);
        return ResponseEntity.ok(chatRoomMessages);
    }

    @PostMapping("update/room-exit-time")
    public ResponseEntity<UpdateChatParticipantResponse> updateChatParticipant(@RequestParam Long chatParticipantId) {
        UpdateChatParticipantResponse updateChatParticipantResponse = chatRoomService.updateChatParticipant(chatParticipantId);
        return ResponseEntity.ok(updateChatParticipantResponse);
    }
}
