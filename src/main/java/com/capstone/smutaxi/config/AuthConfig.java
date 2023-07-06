package com.capstone.smutaxi.config;

import com.capstone.smutaxi.repository.JpaUserRepository;
import com.capstone.smutaxi.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository();
    }

}
