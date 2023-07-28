package com.capstone.smutaxi.exception.matching;

import com.capstone.smutaxi.exception.BusinessException;
import com.capstone.smutaxi.exception.ErrorCode;

public class CannotJoinWaitingRoomException extends BusinessException {

    public CannotJoinWaitingRoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}
