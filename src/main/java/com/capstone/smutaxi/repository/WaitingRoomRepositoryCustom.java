package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.WaitingRoom;

import java.util.List;
import java.util.Optional;

public interface WaitingRoomRepositoryCustom {
    public List<WaitingRoom> findAllWithWaitingRoomUser();
    public Optional<WaitingRoom> findWithWaitingRoomUser(Long waitingRoomId);
}
