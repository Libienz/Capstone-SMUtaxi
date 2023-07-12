package com.capstone.smutaxi.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class VerificationResponse {
    private Boolean sended;
    private Integer verificationCode;
}
