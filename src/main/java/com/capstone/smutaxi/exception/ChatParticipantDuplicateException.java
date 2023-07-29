package com.capstone.smutaxi.exception;

public class ChatParticipantDuplicateException extends BusinessException{

    public ChatParticipantDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
