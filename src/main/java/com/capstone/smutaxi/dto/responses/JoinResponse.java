package com.capstone.smutaxi.dto.responses;

import com.capstone.smutaxi.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class JoinResponse {
    UserDto userDto;
    String token;
}
