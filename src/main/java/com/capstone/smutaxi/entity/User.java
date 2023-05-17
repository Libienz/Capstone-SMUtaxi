package com.capstone.smutaxi.entity;


import lombok.*;
import javax.persistence.*;


@Entity // 이 클래스는 데이터베이스의 테이블과 1:1매핑되는 객체다라고 명시 ORM (Object Relation Mapping)
@Getter @Setter
@Table(name="users") //table 이름이 user가 될 수 없음 그래서 네임 따로 지정해준다
@AllArgsConstructor
@NoArgsConstructor
public class User {

    //email ID primary key
    @Id
    @Column(name = "email")
    private String email;

    //닉네임
    @Column(name = "name")
    private String name;

    //프로필 사진 경로
    @Column(name = "img_path")
    private String imgPath;

    //성별
    @Column(name = "gender")
    private Gender gender;

    //현재 위치: 위도, 경도
    @Embedded
    @Column(name = "location")
    private Location location;

    //택시 합승의 조건: 동성 가능, 최소 출발 인원, 근데 이거 끼면 로직 복잡해질 듯?
    @Embedded
    @Column(name = "condition")
    private TaxiPoolCondition taxiPoolCondition;



}
