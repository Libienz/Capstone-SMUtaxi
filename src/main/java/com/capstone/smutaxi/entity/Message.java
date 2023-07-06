package com.capstone.smutaxi.entity;


import lombok.Getter;

import javax.persistence.*;


@Entity
@Getter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;

    private String senderNickName;

    private String sendTime;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

}
