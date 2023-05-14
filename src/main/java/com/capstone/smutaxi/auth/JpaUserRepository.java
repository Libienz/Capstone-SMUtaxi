package com.capstone.smutaxi.auth;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository{

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<User> findByEmail(String email) {
        User user = em.find(User.class, email);
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.empty();
    }
}
