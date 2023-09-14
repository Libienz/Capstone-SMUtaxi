package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepositoryCustom {
    Optional<ChatRoom> findWithMessageById(Long Id);
}
