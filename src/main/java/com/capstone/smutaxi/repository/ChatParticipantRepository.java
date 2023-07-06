package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, String> {
    List<ChatParticipant> findByUserEmail(String userEmail);
    int countByChatRoomId(Long chatRoomId);
}
