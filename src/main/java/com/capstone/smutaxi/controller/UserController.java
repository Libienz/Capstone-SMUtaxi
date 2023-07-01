package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.ChatRoomUser;
import com.capstone.smutaxi.chat.service.ChatRoomService;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.auth.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;


    public UserController(UserService userService, UserRepository userRepository, ChatRoomService chatRoomService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.chatRoomService = chatRoomService;
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDto){
        userService.updateUser(userDto);
        return ResponseEntity.ok().body(userDto.toString());
    }

    @GetMapping("/chatRooms")
    public List<ChatRoom> getUserChatRooms(@RequestParam String email){

        List<ChatRoom> chatRoomsByUserEmail = chatRoomService.getChatRoomsByUserEmail(email);
        return chatRoomsByUserEmail;

    }
}
