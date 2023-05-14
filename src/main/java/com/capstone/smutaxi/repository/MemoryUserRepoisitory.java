package com.capstone.smutaxi.repository;
import com.capstone.smutaxi.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryUserRepoisitory implements UserRepository {

    private static Map<String, User> store = new HashMap<>(); //concurrent 필요

    @Override
    public User save(User user) {
        store.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(store.get(email));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<User> findByName(String name) {
        return store.values().stream() //store 도는 루프
                .filter(member -> member.getName().equals(name)) //람다 조건에 맞는 member filtering
                .findAny(); //하나라도 찾으면 Optional로 반환 없으면 null
    }

    public void clearStore() {
        store.clear();
    }

}
