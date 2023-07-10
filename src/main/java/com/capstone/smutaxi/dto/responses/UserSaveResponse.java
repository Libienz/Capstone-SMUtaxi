package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class UserSaveResponse {
    private Boolean success;
    private String message;
    private UserDto userDto;
}
