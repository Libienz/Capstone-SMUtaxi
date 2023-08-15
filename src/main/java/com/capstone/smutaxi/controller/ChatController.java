package com.capstone.smutaxi.controller;


import com.capstone.smutaxi.dto.responses.chat.ChatRoomMessageResponse;
import com.capstone.smutaxi.dto.responses.chat.LeaveChatParticipantResponse;
import com.capstone.smutaxi.dto.responses.chat.UpdateChatParticipantResponse;
import com.capstone.smutaxi.dto.responses.chat.UserJoinedChatRoomResponse;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.entity.SystemMessage;
import com.capstone.smutaxi.entity.UserMessage;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatController {
    private final ChatRoomService chatRoomService;

    //채팅방 생성 API
    @PostMapping("")
    public ResponseEntity<String> createChatRoom(@RequestParam String chatRoomName) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomName);
        return ResponseEntity.ok("create chatRoom successfully. chatRoomId: " + chatRoom.getId());
    }

    //채팅방에 유저추가 API
    @PostMapping("{roomId}/add-user")
    public ResponseEntity<String> addChatRoomUser(@PathVariable("roomId") Long chatRoomId, @RequestParam String userEmail) {
        Long chatParticipantId = chatRoomService.addUserToChatRoom(chatRoomId, userEmail);
        return ResponseEntity.ok("User added to the chat room successfully. chatParticipantId: " + chatParticipantId);
    }

    //채팅방에서 유저 나감 API
    @DeleteMapping("{roomId}/participants/{participantId}")
    public ResponseEntity<LeaveChatParticipantResponse> leaveChatParticipant(@PathVariable("roomId") Long chatRoomId, @PathVariable("participantId") Long chatParticipantId) {
        chatRoomService.leaveChatParticipant(chatParticipantId);
        LeaveChatParticipantResponse leaveChatParticipantResponse = LeaveChatParticipantResponse.builder().success(true).message("User leave to the chat room successfully.").build();
        return ResponseEntity.ok(leaveChatParticipantResponse);
    }

    //유저가 참가한 ChatRooms 반환 API
    @GetMapping("")
    public ResponseEntity<UserJoinedChatRoomResponse> getUserChatRooms(@RequestParam String email) {
        UserJoinedChatRoomResponse userJoinedChatRooms = chatRoomService.getUserJoinedChatRooms(email);
        return ResponseEntity.ok(userJoinedChatRooms);
    }

    //채팅방의 messages 반환 api
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ChatRoomMessageResponse> getChatRoomMessages(@PathVariable("roomId") Long chatRoomId, @RequestParam String userEmail) {
        ChatRoomMessageResponse chatRoomMessages = chatRoomService.getChatRoomMessages(userEmail, chatRoomId);
        return ResponseEntity.ok(chatRoomMessages);
    }

    @PutMapping("{roomId}/participants/{participantId}/room-exit-time")
    public ResponseEntity<UpdateChatParticipantResponse> updateChatParticipant(@PathVariable("participantId") Long chatParticipantId) {
        UpdateChatParticipantResponse updateChatParticipantResponse = chatRoomService.updateChatParticipant(chatParticipantId);
        return ResponseEntity.ok(updateChatParticipantResponse);
    }
}
