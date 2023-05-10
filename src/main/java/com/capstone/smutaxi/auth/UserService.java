package com.capstone.smutaxi.auth;


public interface UserService {


    void join(User user);

    boolean login(String id, String pw);
}
