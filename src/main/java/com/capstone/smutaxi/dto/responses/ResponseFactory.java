package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.entity.User;

import static com.capstone.smutaxi.service.auth.UserService.userToUserDto;

public class ResponseFactory {

    //로그인 성공 반환 생성기
    public static LoginResponse createLoginSuccessResponse(User loginedUser, String token) {

        UserDto userDto = userToUserDto(loginedUser);
        userDto.setPassword(loginedUser.getPassword());
        LoginResponse loginResponse = LoginResponse
                .builder()
                .userDto(userDto)
                .error(null)
                .token(token)
                .build();
        return loginResponse;
    }
}
