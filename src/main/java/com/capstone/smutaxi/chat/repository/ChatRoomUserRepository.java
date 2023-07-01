package com.capstone.smutaxi.chat.repository;

import com.capstone.smutaxi.chat.domain.ChatRoomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Repository
public class ChatRoomUserRepository {

    private final EntityManager em;

    @Transactional
    public void save(ChatRoomUser chatRoomUser) {
        em.persist(chatRoomUser);
    }
}
