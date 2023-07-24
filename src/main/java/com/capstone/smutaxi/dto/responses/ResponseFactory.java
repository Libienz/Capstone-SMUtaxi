package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.UserDto;

public class ResponseFactory {

    public static JoinResponse createJoinResponse(Boolean success, String message, UserDto userDto, String token) {
        return JoinResponse.builder()
                .success(true)
                .message(null)
                .userDto(userDto)
                .token(token)
                .build();
    }

    public static LoginResponse createLoginResponse(Boolean success, String message, UserDto userDto, String token) {
        return LoginResponse.builder()
                .success(true)
                .message(null)
                .userDto(userDto)
                .token(token)
                .build();
    }
}
