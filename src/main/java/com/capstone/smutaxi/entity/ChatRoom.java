package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.enums.GenderRestriction;
import com.capstone.smutaxi.utils.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@Entity
@Table(name = "chat_room")
public class ChatRoom  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    private Location location;


    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.name = name;
        return  chatRoom;
    }




}
