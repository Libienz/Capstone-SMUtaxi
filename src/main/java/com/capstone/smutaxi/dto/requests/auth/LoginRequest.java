package com.capstone.smutaxi.dto.requests.auth;

import lombok.Getter;

@Getter
public class LoginRequest {

//    @NotNull
//    @Size(min = 3, max = 100)
    private String email;

//    @NotNull
//    @Size(min = 8, max = 300)
    private String password;

}
