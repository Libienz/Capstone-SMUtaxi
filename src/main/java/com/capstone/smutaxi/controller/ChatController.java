package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.dto.responses.ChatRoomMessageResponse;
import com.capstone.smutaxi.dto.responses.UserJoinedChatRoomResponse;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.repository.MessageRepository;
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
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void chat(Message message) {
        Message msg = messageService.saveMessage(message);
        System.out.println("msgid = " + msg.getId());
        messagingTemplate.convertAndSend("/sub/channel/" + message.getChatRoom().getId(), message);
    }
    //채팅방 생성 API
    @PostMapping("/create-chatRoom")
    public ResponseEntity<String> createChatRoom(@RequestParam String chatRoomName){
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomName);
        return ResponseEntity.ok("create chatRoom successfully. chatRoomId: "+chatRoom.getId());
    }

    //채팅방에 유저추가 API
    @PostMapping("/add-user")
    public ResponseEntity<String> addChatRoomUser(@RequestParam String userEmail, @RequestParam Long chatRoomId) {
        Long chatParticipantId = chatRoomService.addUserToChatRoom(chatRoomId, userEmail);
        return ResponseEntity.ok("User added to the chat room successfully. chatParticipantId: "+chatParticipantId);
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leaveChatParticipant(@RequestParam Long chatParticipantId) {
        chatRoomService.leaveChatParticipant(chatParticipantId);
        return ResponseEntity.ok("User leave to the chat room successfully.");
    }

    //유저가 참가한 ChatRoom의 이름과 Id 반환 API
    @GetMapping("/user/chatRooms")
    public ResponseEntity<UserJoinedChatRoomResponse> getUserChatRooms(@RequestParam String email){
        UserJoinedChatRoomResponse userJoinedChatRooms = chatRoomService.getUserJoinedChatRooms(email);
        return ResponseEntity.ok(userJoinedChatRooms);
    }

    @GetMapping("/chatRoom/messages")
    public ResponseEntity<ChatRoomMessageResponse> getChatRoomMessages(@RequestParam String userEmail, @RequestParam Long chatRoomId){
        ChatRoomMessageResponse chatRoomMessages = chatRoomService.getChatRoomMessages(userEmail, chatRoomId);
        return ResponseEntity.ok(chatRoomMessages);
    }

}
