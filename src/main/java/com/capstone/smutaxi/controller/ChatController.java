package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.dto.responses.ChatRoomResponse;
import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void chat(Message message) {
        messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getChatRoom().getId(), message);
    }

    //채팅방에 유저추가 API
    @PostMapping("/add-user")
    public ResponseEntity<String> addChatRoomUser(@RequestParam String userEmail, @RequestParam Long chatRoomId) {
        chatRoomService.addUserToChatRoom(chatRoomId, userEmail);
        return ResponseEntity.ok("User added to the chat room successfully.");
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leaveChatParticipant(@RequestParam Long chatParticipantId) {
        chatRoomService.leaveChatParticipant(chatParticipantId);
        return ResponseEntity.ok("User leave to the chat room successfully.");
    }

    //유저가 참가한 ChatRoom의 이름과 Id 반환 API
    @GetMapping("/user/chatRooms")
    public List<ChatRoomResponse> getUserChatRooms(@RequestParam String email){
        List<ChatRoomResponse> chatRoomResponses = chatRoomService.getUserJoinedChatRooms(email);
        return chatRoomResponses;
    }

}
