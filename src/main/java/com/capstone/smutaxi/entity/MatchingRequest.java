package com.capstone.smutaxi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matching_requests")
@Getter @Setter
public class MatchingRequest {

    //요청 식별 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //매칭 요청자
    @Column(name = "user_id")
    private String userId;

    //요청 위치
    @Embedded
    @Column(name = "location")
    private Location location;

    //후에 Criteria Builder가 필요하면 사용
//    @Column(name = "criteria")
//    private String criteria;

    //요청 시각
    //단일 값으로 취급 됨으로 @Embedded 어노테이션 필요 X
    @Column(name = "created_at")
    private LocalDateTime createdAt;

//
//    @Column(name = "status")
//    private String status;


}
