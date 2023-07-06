package com.capstone.smutaxi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 채팅방과 유저의 다대다 관계를 중간 테이블을 entity로 승격하여 구현한 것
 * 유저 to 채팅방 관계는 참여
 * 채팅방 to 유저의 관계는 참가관리
 */
@Getter
@Setter
@Entity
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn
    private User user;
}
