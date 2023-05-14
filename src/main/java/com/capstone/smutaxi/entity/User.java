package com.capstone.smutaxi.entity;


import lombok.*;
import javax.persistence.*;


@Entity // 이 클래스는 데이터베이스의 테이블과 1:1매핑되는 객체다라고 명시 ORM (Object Relation Mapping)
@Getter @Setter
@Table(name="users") //table 이름이 user가 될 수 없음 그래서 네임 따로 지정해준다
@AllArgsConstructor
@NoArgsConstructor
public class User {


    @Id //primary key임을 명시
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "img_path")
    private String imgPath;

}
