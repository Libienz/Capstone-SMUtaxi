package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.QWaitingRoomUser;
import com.capstone.smutaxi.entity.WaitingRoomUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.capstone.smutaxi.entity.QChatParticipant.chatParticipant;
import static com.capstone.smutaxi.entity.QChatRoom.chatRoom;
import static com.capstone.smutaxi.entity.QUser.user;
import static com.capstone.smutaxi.entity.QWaitingRoomUser.*;

public class WaitingRoomUserRepositoryImpl implements WaitingRoomUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WaitingRoomUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<WaitingRoomUser> getUserRequest(String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(waitingRoomUser)
                        .join(waitingRoomUser.user, user)
                        .where(user.email.eq(email))
                        .fetchOne());
    }
}
