package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.Message;
import com.capstone.smutaxi.chat.service.ChatRoomService;
import com.capstone.smutaxi.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {
    private final ChatRoomService chatRoomService;
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

}
