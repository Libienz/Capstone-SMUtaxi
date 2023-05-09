package com.capstone.smutaxi.auth;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepoisitory implements MemberRepository {

    private static Map<String, Member> store = new HashMap<>(); //concurrent 필요

    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return null;
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() //store 도는 루프
                .filter(member -> member.getName().equals(name)) //람다 조건에 맞는 member filtering
                .findAny(); //하나라도 찾으면 반환 없으면 null
    }

}
