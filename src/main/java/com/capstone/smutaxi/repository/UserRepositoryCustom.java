package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findWithChatParticipantByEmail(String email);
}
