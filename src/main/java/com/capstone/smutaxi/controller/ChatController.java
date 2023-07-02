package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.Message;
import com.capstone.smutaxi.chat.service.ChatRoomService;
import com.capstone.smutaxi.chat.service.ChatRoomUserService;
import com.capstone.smutaxi.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void chat(Message message) {
        messageService.sendMessage(message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getChatRoom().getId(), message);
    }

    @PostMapping("/chat/room")
    public ChatRoom createRoom(@RequestParam String name){
        return chatRoomService.createChatRoom(name);
    }

    @GetMapping("/chat/room")
    public List<ChatRoom> getChatRoomList() {
        return chatRoomService.getRoomList();
    }

    @PostMapping("/chat/addUser")
    public ResponseEntity<String> addChatRoomUser(@RequestParam String userEmail, @RequestParam Long chatRoomId) {
        try {
            chatRoomUserService.addChatRoomUser(userEmail, chatRoomId);
            return ResponseEntity.ok("User added to the chat room successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/chat/chatRooms")
    public List<ChatRoom> getUserChatRooms(@RequestParam String email){
        //어째서 무환 순환 참조가?? 일단은 @JsonIgnore로 틀어 막았음.
        List<ChatRoom> chatRoomsByUserEmail = chatRoomService.getChatRoomsByUserEmail(email);
        return chatRoomsByUserEmail;

    }

}
