package com.capstone.smutaxi.chat.domain;


import com.capstone.smutaxi.entity.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



@Getter
@Setter
@Entity
@Table(name = "chat_room")
public class ChatRoom implements Comparable<ChatRoom> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    private GenderRestriction genderRestriction; //일단 제외

    private Location location;

    /**
     * 고민인점
     * @ElementCollection을 쓸것이냐 User하고 @OneToMany / @ManyToOne 관계를 맺을것이냐...
     * https://prohannah.tistory.com/133
     * 결론적으로 중간 테이블을 이용한 다대다 관계 적용
     */
    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomUser> chatRoomUsers = new ArrayList<>();


    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.name = name;
        return  chatRoom;
    }
    public static ChatRoom create(String name,GenderRestriction genderRestriction) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.name = name;
        chatRoom.genderRestriction =genderRestriction;
        return  chatRoom;
    }

    public void addChatRoomUser(ChatRoomUser chatRoomUser) {
        chatRoomUsers.add(chatRoomUser);
        chatRoomUser.setChatRoom(this);
    }

    @Override
    public int compareTo(ChatRoom o) {
        int thisSize = this.chatRoomUsers.size();
        int otherSize = o.chatRoomUsers.size();

        // 내림차순 정렬을
        return Integer.compare(otherSize, thisSize);
    }

}
