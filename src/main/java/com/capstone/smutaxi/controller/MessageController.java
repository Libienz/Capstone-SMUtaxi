package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.Message;
import com.capstone.smutaxi.chat.service.ChatRoomService;
import com.capstone.smutaxi.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    //private final SimpMessageSendingOperations messageSendingOperations;

    @MessageMapping("/chat/send")
    public void chat(Message message) {
        messageService.sendMessage(message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getChatRoom().getId(), message);
        //messageSendingOperations.convertAndSend("/sub/channel/" + params.get("channelId"), params);
    }

    @PostMapping("/chat/enter")
    public Long JoinChatRoom(@RequestParam Long id) {
        try {
            ChatRoom chatRoom = chatRoomService.joinChatRoom(id);
            return chatRoom.getId();
        } catch(IllegalStateException e) {
            return 400L;
        }
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
