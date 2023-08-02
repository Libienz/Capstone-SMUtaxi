package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.utils.Location;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@Entity
@Table(name = "chat_rooms")
public class ChatRoom  {
    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatRoomName;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatParticipant> chatRoomParticipant = new ArrayList<>();

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomName = name;
        return  chatRoom;
    }




}
