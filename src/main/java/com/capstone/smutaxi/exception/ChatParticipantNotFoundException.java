package com.capstone.smutaxi.exception;

public class ChatParticipantNotFoundException extends BusinessException{
    public ChatParticipantNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
