package com.capstone.smutaxi.auth;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(String id);

    Member save(Member member);

    List<Member> findAll();

    Optional<Member> findByName(String name);


}
