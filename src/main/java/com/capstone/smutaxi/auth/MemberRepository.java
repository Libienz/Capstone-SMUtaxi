package com.capstone.smutaxi.auth;

public interface MemberRepository {

    Member findById(String id);

    void save(Member member);
}
