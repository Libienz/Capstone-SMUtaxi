package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.capstone.smutaxi.entity.QChatParticipant.chatParticipant;
import static com.capstone.smutaxi.entity.QUser.user;

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<User> findWithChatParticipantQuerydslByEmail(String email) {
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .distinct()
                .join(user.chatParticipantList, chatParticipant).fetchJoin()
                .where(user.email.eq(email))
                .fetchOne());
    }
}
