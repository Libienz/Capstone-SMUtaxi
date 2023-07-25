package com.capstone.smutaxi.exception.user;

import com.capstone.smutaxi.exception.BusinessException;
import com.capstone.smutaxi.exception.ErrorCode;

public class IdDuplicateException extends BusinessException {

    public IdDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
