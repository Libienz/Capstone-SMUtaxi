package com.capstone.smutaxi.service;


import com.capstone.smutaxi.entity.User;

public interface UserService {


    void join(User user);

    boolean login(String id, String pw);
}
