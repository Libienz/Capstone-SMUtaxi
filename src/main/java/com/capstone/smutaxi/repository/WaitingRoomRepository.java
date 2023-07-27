package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.WaitingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingRoomRepository {

    private final EntityManager em;

    public WaitingRoom findById(Long id) {
        WaitingRoom waitingRoom = em.find(WaitingRoom.class, id);
        return waitingRoom;
    }

    public Long save(WaitingRoom waitingRoom) {
        em.persist(waitingRoom);
        return waitingRoom.getId();
    }

    public List<WaitingRoom> findAll() {
        return em.createQuery("select r from waitingroom r", WaitingRoom.class)
                .getResultList();
    }



}
