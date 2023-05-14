package com.capstone.smutaxi.repository;

import com.capstone.smutaxi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String id);

    User save(User user);

    List<User> findAll();

    Optional<User> findByName(String name);


}
