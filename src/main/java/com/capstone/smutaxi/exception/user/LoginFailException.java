package com.capstone.smutaxi.exception.user;

import com.capstone.smutaxi.exception.BusinessException;
import com.capstone.smutaxi.exception.ErrorCode;

public class LoginFailException extends BusinessException {

    public LoginFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
