package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.enums.Gender;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String imgUrl;
    private String name;
    private Gender gender;
    private String deviceToken;

    public UserDto(String email, String password, String imgUrl, String name, Gender gender) {
        this.email = email;
        this.password = password;
        this.imgUrl = imgUrl;
        this.name = name;
        this.gender = gender;
    }
}
