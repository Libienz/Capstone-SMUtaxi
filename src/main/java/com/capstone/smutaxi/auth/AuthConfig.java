package com.capstone.smutaxi.auth;

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
