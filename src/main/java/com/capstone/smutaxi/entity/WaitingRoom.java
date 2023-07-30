package com.capstone.smutaxi.entity;

import com.capstone.smutaxi.utils.Location;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingRoom implements Comparable<WaitingRoom> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_room_id")
    private Long id;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "waitingRoom", cascade = CascadeType.ALL)
    List<WaitingRoomUser> waiters = new ArrayList<>();

    //==생성 메서드==//
    public static WaitingRoom createWaitingRoom() {
        WaitingRoom waitingRoom = new WaitingRoom();
        return waitingRoom;
    }

    @Override
    public int compareTo(@NotNull WaitingRoom o) {
        return o.getWaiters().size() - this.getWaiters().size();
    }
    //==비즈니스 로직==//
    //notify match success

}
