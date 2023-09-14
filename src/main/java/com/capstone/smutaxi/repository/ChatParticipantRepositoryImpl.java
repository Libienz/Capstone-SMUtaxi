package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.capstone.smutaxi.entity.QChatParticipant.chatParticipant;
import static com.capstone.smutaxi.entity.QChatRoom.chatRoom;
import static com.capstone.smutaxi.entity.QUser.user;


public class ChatParticipantRepositoryImpl implements ChatParticipantRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChatParticipantRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<ChatParticipant> findQuerydslById(Long Id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(chatParticipant)
                        .join(chatParticipant.user, user).fetchJoin()
                        .join(chatParticipant.chatRoom, chatRoom).fetchJoin()
                        .where(chatParticipant.id.eq(Id))
                        .fetchOne());

    }
}
