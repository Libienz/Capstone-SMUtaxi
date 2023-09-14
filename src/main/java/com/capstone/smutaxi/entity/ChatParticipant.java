package com.capstone.smutaxi.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

/**
 * 채팅방과 유저의 다대다 관계를 중간 테이블을 entity로 승격하여 구현한 것
 * 유저 to 채팅방 관계는 참여
 * 채팅방 to 유저의 관계는 참가관리
 */
@Getter
@Setter
@Entity
@Table(name = "chat_participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipant {
    @Id
    @Column(name = "chat_participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime lastLeaveTime;

    //==생성 메서드==//
    public static ChatParticipant createChatParticipant(ChatRoom chatRoom, User user, LocalDateTime lastLeaveTime) {
        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setChatRoom(chatRoom);
        chatParticipant.setUser(user);
        chatParticipant.setLastLeaveTime(lastLeaveTime);
        return chatParticipant;
    }
    //==연관관계 메서드==//
    public void setChatRoomAndUser(ChatRoom chatRoom, User user){
        this.chatRoom = chatRoom;
        this.user = user;
    }
    public void remove() {
        User user = this.getUser();
        ChatRoom room = this.getChatRoom();

        if (user != null) {
            user.getChatParticipantList().remove(this);
            this.setUser(null);
        }

        if (room != null) {
            room.getChatRoomParticipant().remove(this);
            this.setChatRoom(null);
        }
    }
}
