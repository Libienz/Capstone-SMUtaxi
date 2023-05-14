package com.capstone.smutaxi.auth;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String id);

    User save(User user);

    List<User> findAll();

    Optional<User> findByName(String name);


}
