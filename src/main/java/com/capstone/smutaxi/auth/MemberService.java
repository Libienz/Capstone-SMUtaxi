package com.capstone.smutaxi.auth;

import java.util.Optional;

public interface MemberService {


    void join(Member member);

    boolean login(String id, String pw);
}
