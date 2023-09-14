package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.WaitingRoom;

import java.util.List;

public interface WaitingRoomRepositoryCustom {
    public List<WaitingRoom> findAllWithWaitingRoomUser();

}
