package com.capstone.smutaxi.exception.user;

import com.capstone.smutaxi.exception.BusinessException;
import com.capstone.smutaxi.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
