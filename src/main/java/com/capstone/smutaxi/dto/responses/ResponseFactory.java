package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.auth.EmailVerificationResponse;
import com.capstone.smutaxi.dto.responses.auth.JoinResponse;
import com.capstone.smutaxi.dto.responses.auth.LoginResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponseDto;
import com.capstone.smutaxi.dto.responses.user.UploadImageResponse;
import com.capstone.smutaxi.dto.responses.user.UserUpdateResponse;

public class ResponseFactory {

    public static JoinResponse createJoinResponse(Boolean success, String message, UserDto userDto, String token) {
        return JoinResponse.builder()
                .success(success)
                .message(message)
                .userDto(userDto)
                .token(token)
                .build();
    }

    public static LoginResponse createLoginResponse(Boolean success, String message, UserDto userDto, String token) {
        return LoginResponse.builder()
                .success(success)
                .message(message)
                .userDto(userDto)
                .token(token)
                .build();
    }

    public static UserUpdateResponse createUserUpdateResponse(Boolean success, String message, UserDto userDto) {
        return UserUpdateResponse.builder()
                .success(success)
                .message(message)
                .userDto(userDto)
                .build();
    }

    public static UploadImageResponse createUploadImageResponse(Boolean success, String message, String imageUrl) {
        return UploadImageResponse.builder()
                .success(success)
                .message(message)
                .imageUrl(imageUrl)
                .build();
    }

    public static EmailVerificationResponse createEmailVerificationResponse(Boolean success, String message, Integer verificationCode) {
        return EmailVerificationResponse.builder().
                success(success).
                message(message).
                verificationCode(verificationCode).
                build();
    }

    public static ErrorResponse createErrorResponse(String error, String message) {
        return ErrorResponse.builder().
                error(error).
                message(message).
                build();
    }

    public static MatchingResponseDto createMatchingResponse(Boolean success, String message, Long waitingRoomId) {
        return MatchingResponseDto.builder().
                success(success).
                message(message).
                waitingRoomId(waitingRoomId).
                build();
    }
}
