package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.QWaitingRoom;
import com.capstone.smutaxi.entity.QWaitingRoomUser;
import com.capstone.smutaxi.entity.WaitingRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.capstone.smutaxi.entity.QChatRoom.chatRoom;
import static com.capstone.smutaxi.entity.QMessage.message1;
import static com.capstone.smutaxi.entity.QWaitingRoom.*;
import static com.capstone.smutaxi.entity.QWaitingRoomUser.waitingRoomUser;

public class WaitingRoomRepositoryImpl implements WaitingRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WaitingRoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<WaitingRoom> findAllWithWaitingRoomUser() {
        return queryFactory
                .selectFrom(waitingRoom)
                .join(waitingRoom.waiters, waitingRoomUser).fetchJoin()
                .orderBy(waitingRoom.waiters.size().desc())
                .distinct()
                .fetch();
    }

    @Override
    public Optional<WaitingRoom> findWithWaitingRoomUser(Long waitingRoomId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(waitingRoom)
                        .join(waitingRoom.waiters, waitingRoomUser).fetchJoin()
                        .where(waitingRoom.id.eq(waitingRoomId))
                        .fetchOne());
    }
}
