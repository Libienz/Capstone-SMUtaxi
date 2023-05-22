package com.capstone.smutaxi.chat.repository;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final EntityManager em;

    public Long save(ChatRoom chatRoom) {
        em.persist(chatRoom);
        return chatRoom.getId();
    }

    public Optional<ChatRoom> findById(Long id) {
        return Optional.ofNullable(em.find(ChatRoom.class, id));
    }

    public List<ChatRoom> findAll() {
        return em.createQuery("SELECT r FROM ChatRoom r", ChatRoom.class)
                .getResultList();
    }
}
