package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.WaitingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public interface WaitingRoomRepository extends JpaRepository<WaitingRoom, Long>, WaitingRoomRepositoryCustom {

}

