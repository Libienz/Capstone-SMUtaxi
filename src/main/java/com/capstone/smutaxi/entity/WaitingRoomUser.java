package com.capstone.smutaxi.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingRoomUser {

    @Column(name = "waiting_room_user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "waiting_room_id")
    private WaitingRoom waitingRoom;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //==생성메서드==//
    public static WaitingRoomUser createWaitingRoomUser(WaitingRoom waitingRoom, User user) {
        WaitingRoomUser waitingRoomUser = new WaitingRoomUser();
        waitingRoomUser.setWaitingRoom(waitingRoom);
        waitingRoomUser.setUser(user);
        return waitingRoomUser;
    }


}
