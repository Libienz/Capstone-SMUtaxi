package com.capstone.smutaxi.exception.user;

import com.capstone.smutaxi.exception.BusinessException;
import com.capstone.smutaxi.exception.ErrorCode;

public class EmailFailToSendException extends BusinessException {

    public EmailFailToSendException(ErrorCode errorCode) {
        super(errorCode);
    }
}
