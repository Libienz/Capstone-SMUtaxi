package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.WaitingRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRoomUserRepository extends JpaRepository<WaitingRoomUser, Long>, WaitingRoomUserRepositoryCustom {

}
