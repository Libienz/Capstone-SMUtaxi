package com.capstone.smutaxi.entity;

import com.capstone.smutaxi.utils.Location;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class WaitingRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_room_id")
    private Long id;

    @Embedded
    private Location location;

    @ElementCollection
    List<String> waiters = new ArrayList<>();

    //==비즈니스 로직==//
    //notify match success

}
