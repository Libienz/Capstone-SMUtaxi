package com.capstone.smutaxi.chat.domain;


import com.capstone.smutaxi.entity.Location;
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
     */
    @ElementCollection
    private List<String> userIdList;


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

    @Override
    public int compareTo(ChatRoom o) {
        int thisSize = this.userIdList.size();
        int otherSize = o.userIdList.size();

        // 내림차순 정렬을
        return Integer.compare(otherSize, thisSize);
    }
}
