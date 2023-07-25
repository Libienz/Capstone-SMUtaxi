package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Optional<User> findByEmail(String email) {
        User user = em.find(User.class, email);
        return Optional.ofNullable(user);
    }

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public List<User> findAll() {
        return null;
    }

    public Optional<User> findByName(String name) {
        return Optional.empty();
    }
}
