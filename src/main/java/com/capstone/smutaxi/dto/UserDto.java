package com.capstone.smutaxi.dto;

import com.capstone.smutaxi.enums.Gender;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String imgUrl;
    private String name;
    private Gender gender;


}
