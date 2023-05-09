package com.capstone.smutaxi.auth;

public interface MemberService {
    void join(Member member);

    Member findMember(String id);

    boolean login(String id, String pw);
}
