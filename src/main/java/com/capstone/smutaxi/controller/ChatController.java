package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.ChatRoom;
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
        messageService.sendMessage(message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getChatRoom().getId(), message);
    }

    @PostMapping("/add-user")
    public ResponseEntity<String> addChatRoomUser(@RequestParam String userEmail, @RequestParam Long chatRoomId) {
        try {
            chatRoomService.addUserToChatRoom(chatRoomId, userEmail);
            return ResponseEntity.ok("User added to the chat room successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user/chatRooms")
    public List<ChatRoom> getUserChatRooms(@RequestParam String email){
        List<ChatRoom> chatRoomsByUserEmail = chatRoomService.getUserJoinedChatRooms(email);
        return chatRoomsByUserEmail;

    }

}
