package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.dto.responses.UserUpdateResponse;
import com.capstone.smutaxi.service.auth.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/update")
@RequiredArgsConstructor
@RestController
public class UserUpdateController {
    private final UserUpdateService userUpdateService;

    //유저 업데이트(모든 필드) API
    @PutMapping("/users/{email}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable("email") String email, @RequestBody UserDto updateDto) {
        UserUpdateResponse userUpdateResponse = userUpdateService.updateUser(email, updateDto);
        return ResponseEntity.ok().body(userUpdateResponse);
    }

    //유저 비밀번호 변경 API
    @PutMapping("/users/{email}/password")
    public ResponseEntity<UserUpdateResponse> updateUserPassword(@PathVariable("email") String email, @RequestBody UserDto updateDto) {
        UserUpdateResponse userUpdateResponse = userUpdateService.updateUserPassword(email, updateDto);
        return ResponseEntity.ok().body(userUpdateResponse);

    }
}
