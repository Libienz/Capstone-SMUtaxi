package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.MessageDto;
import com.capstone.smutaxi.dto.UserJoinedChatRoomDto;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.auth.EmailVerificationResponse;
import com.capstone.smutaxi.dto.responses.auth.JoinResponse;
import com.capstone.smutaxi.dto.responses.auth.LoginResponse;
import com.capstone.smutaxi.dto.responses.chat.ChatRoomMessageResponse;
import com.capstone.smutaxi.dto.responses.chat.UserJoinedChatRoomResponse;
import com.capstone.smutaxi.dto.responses.match.MatchCancelResponse;
import com.capstone.smutaxi.dto.responses.match.MatchingResponse;
import com.capstone.smutaxi.dto.responses.rally.RallyInformationDto;
import com.capstone.smutaxi.dto.responses.rally.RallyResponse;
import com.capstone.smutaxi.dto.responses.user.UploadImageResponse;
import com.capstone.smutaxi.dto.responses.user.UserUpdateResponse;

import java.util.List;

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

    public static MatchCancelResponse createMatchCancelResponse(Boolean success, String message) {
        return MatchCancelResponse.builder()
                .success(success)
                .message(message)
                .build();
    }
    public static RallyResponse createRallyResponse(Boolean success, String message, RallyInformationDto rallyInformationDto){
        return RallyResponse.builder()
                .success(success)
                .message(message)
                .rallyInformationDto(rallyInformationDto)
                .build();
    }

    public static UserJoinedChatRoomResponse createChatRoomResponse(Boolean success, String message, List<UserJoinedChatRoomDto> userJoinedChatRoomDtoList){
        return UserJoinedChatRoomResponse.builder()
                .success(success)
                .message(message)
                .userJoinedChatRoomDtoList(userJoinedChatRoomDtoList)
                .build();
    }

    public static ChatRoomMessageResponse createChatRoomMessageResponse(Boolean success, String message, Long chatParticipantId, List<MessageDto> messageDtoList){
        return ChatRoomMessageResponse.builder()
                .success(success)
                .message(message)
                .chatParticipantId(chatParticipantId)
                .messageDtoList(messageDtoList)
                .build();
    }

    public static ErrorResponse createErrorResponse(String error, String message) {
        return ErrorResponse.builder().
                error(error).
                message(message).
                build();
    }

    public static MatchingResponse createMatchingResponse(Boolean success, String message, Long waitingRoomId, Long waitingRoomUserId) {
        return MatchingResponse.builder().
                success(success).
                message(message).
                waitingRoomId(waitingRoomId).
                waitingRoomUserId(waitingRoomUserId).
                build();
    }
}
