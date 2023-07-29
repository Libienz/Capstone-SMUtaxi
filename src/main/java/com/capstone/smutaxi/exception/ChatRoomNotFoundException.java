package com.capstone.smutaxi.exception;

public class ChatRoomNotFoundException extends BusinessException{

    public ChatRoomNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
