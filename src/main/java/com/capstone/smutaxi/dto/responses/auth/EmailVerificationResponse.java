package com.capstone.smutaxi.dto.responses.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class EmailVerificationResponse {
    private Boolean success;
    private String message;
    private Integer verificationCode;
}
