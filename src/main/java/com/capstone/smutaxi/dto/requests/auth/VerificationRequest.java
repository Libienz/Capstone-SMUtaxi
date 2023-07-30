package com.capstone.smutaxi.dto.requests.auth;

import lombok.Getter;

@Getter
public class VerificationRequest {

    Boolean noFoundThenSend;
    Boolean FoundThenSend;
}
