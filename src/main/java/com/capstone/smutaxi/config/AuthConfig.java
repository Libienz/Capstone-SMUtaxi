package com.capstone.smutaxi.config;

import com.capstone.smutaxi.service.BasicUserService;
import com.capstone.smutaxi.repository.JpaUserRepository;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public UserService userService() {
        return new BasicUserService(userRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository();
    }


}
