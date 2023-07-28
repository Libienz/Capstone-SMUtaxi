package com.capstone.smutaxi.dto.responses.auth;

import com.capstone.smutaxi.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class JoinResponse {
    private Boolean success;
    private String message;
    private UserDto userDto;
    private String token;
}
