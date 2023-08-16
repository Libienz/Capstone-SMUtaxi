package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.user.UserUpdateResponse;
import com.capstone.smutaxi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    //유저 업데이트(모든 필드) API
    @PutMapping("/{email}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable("email") String email, @RequestBody UserDto updateDto) {
        UserUpdateResponse userUpdateResponse = userService.updateUser(email, updateDto);
        return ResponseEntity.ok().body(userUpdateResponse);
    }

    //유저 비밀번호 변경 API
    @PutMapping("/{email}/password")
    public ResponseEntity<UserUpdateResponse> updateUserPassword(@PathVariable("email") String email, @RequestBody UserDto updateDto) {
        UserUpdateResponse userUpdateResponse = userService.updateUserPassword(email, updateDto);
        return ResponseEntity.ok().body(userUpdateResponse);

    }
}
