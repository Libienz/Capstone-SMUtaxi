package com.capstone.smutaxi.entity;


import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.enums.Gender;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.*;


@Entity // 이 클래스는 데이터베이스의 테이블과 1:1매핑되는 객체다라고 명시 ORM (Object Relation Mapping)
@Getter @Setter
@Table(name="users") //table 이름이 user가 될 수 없음 그래서 네임 따로 지정해준다
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    private String email;

    private String password;

    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipantList = new ArrayList<>();

    //역할기반제어를 위한 roles필드 (Spring Security)
    @ElementCollection(fetch = EAGER) //roles 컬렉션
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    //Spring Security Method
    @Override   //사용자의 권한 목록 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //==생성 메서드==//
    public static User createUser(String email, String password, String name, String imageUrl) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setImageUrl(imageUrl);
//        user.setGender(gender);

        return user;
    }
    //Spring Security Method
    @Override
    public String getUsername() {
        return email;
    }

    //Spring Security Method
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Spring Security Method
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Spring Security Method
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Spring Security Method
    @Override
    public boolean isEnabled() {
        return true;
    }

    //user에서 필요한 정보만 추려 전송용 객체 생성
    public UserDto userToUserDto() {
        UserDto userDto = new UserDto(this.getEmail(), this.getPassword(), this.getImageUrl(), this.getName(), this.getGender());
        return userDto;
    }


}
