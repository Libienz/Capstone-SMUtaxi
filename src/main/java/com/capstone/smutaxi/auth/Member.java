package com.capstone.smutaxi.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private String id;
    private String password;
    private String name;

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
