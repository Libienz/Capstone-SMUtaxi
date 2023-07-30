package com.capstone.smutaxi.dto.responses.auth;

import com.capstone.smutaxi.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class LoginResponse {
    private Boolean success;
    private String message;
    private String token;
    private UserDto userDto;
}
