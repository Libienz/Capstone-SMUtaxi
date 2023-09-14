package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatParticipant;

import java.util.Optional;

public interface ChatParticipantRepositoryCustom {

    Optional<ChatParticipant> findQuerydslById(Long Id);
}
