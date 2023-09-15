package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.WaitingRoomUser;

import java.util.Optional;

public interface WaitingRoomUserRepositoryCustom {

    public Optional<WaitingRoomUser> getUserRequest(String email);
}
