package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findWithChatParticipantQuerydslByEmail(String email);
}
