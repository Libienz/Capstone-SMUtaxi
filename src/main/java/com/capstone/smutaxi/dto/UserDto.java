package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class UserDto {
    private String email;
    private String password;
    private String imgUri;
    private String name;
    private Gender gender;
}
