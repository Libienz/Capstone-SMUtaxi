package com.capstone.smutaxi.dto.requests;

import lombok.Getter;

@Getter
public class VerificationRequest {

    Boolean noFoundThenSend;
    Boolean FoundThenSend;
}
