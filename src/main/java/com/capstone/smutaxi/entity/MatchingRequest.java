package com.capstone.smutaxi.entity;

import com.capstone.smutaxi.utils.Location;
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
    @Column(name = "matching_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //매칭 요청자
    private String requesterEmail;

    //요청 위치
    @Embedded
    private Location location;

    //요청 시각
    //단일 값으로 취급 됨으로 @Embedded 어노테이션 필요 X
    private LocalDateTime createdAt;




}
