package com.capstone.smutaxi.chat.repository;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final EntityManager em;

    public Long save(ChatRoom chatRoom) {
        em.persist(chatRoom);
        return chatRoom.getId();
    }

    public Optional<ChatRoom> findById(Long id) {
        return Optional.ofNullable(em.find(ChatRoom.class, id));
    }

    public List<ChatRoom> findAll() {
        return em.createQuery("SELECT r FROM ChatRoom r", ChatRoom.class)
                .getResultList();
    }

    //add user Error Handling 필요할 듯
    //채팅방에 인원이 꽉 찼는데 넣을려고 했다거나 등등
    @Transactional
    public void addUser(Long chatRoomId, String userId) {
        ChatRoom chatRoom = em.find(ChatRoom.class, chatRoomId);
        if (chatRoom != null) {
            chatRoom.getUserIdList().add(userId);
            em.flush(); // 변경 사항을 DB에 즉시 반영
        }
    }

    @Transactional
    public void initLocation(Long chatRoomId, Location location) {
        ChatRoom chatRoom = em.find(ChatRoom.class, chatRoomId);
        if (chatRoom != null) {
            chatRoom.setLocation(location);
            em.flush(); // 변경 사항을 DB에 즉시 반영
        }
    }

}
