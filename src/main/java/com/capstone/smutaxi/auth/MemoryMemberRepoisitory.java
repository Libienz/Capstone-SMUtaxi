package com.capstone.smutaxi.auth;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepoisitory implements MemberRepository {

    private static Map<String, Member> store = new HashMap<>(); //concurrent 필요

    @Override
    public Member findById(String id) {
        return store.get(id);
    }

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }
}
