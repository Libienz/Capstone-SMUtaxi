package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final EntityManager em;

    public void save(Message message) {
        em.persist(message);
    }
}

