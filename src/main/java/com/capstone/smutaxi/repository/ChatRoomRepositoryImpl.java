package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.capstone.smutaxi.entity.QChatRoom.chatRoom;
import static com.capstone.smutaxi.entity.QMessage.message1;

public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<ChatRoom> findWithMessageById(Long Id) {
        return Optional.ofNullable(queryFactory.selectFrom(chatRoom)
                .leftJoin(chatRoom.messageList, message1).fetchJoin()
                .where(chatRoom.id.eq(Id))
                .fetchOne());
    }
}
