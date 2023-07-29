package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, String> {
    List<ChatParticipant> findByUserEmail(String userEmail);
    Optional<ChatParticipant> findByUserEmailAndChatRoomId(String userEmail, Long chatRoomId);
    Optional<ChatParticipant> findById(Long Id);
    int countByChatRoomId(Long chatRoomId);
}
