package com.capstone.smutaxi.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public UserService memberService() {
        return new BasicUserService(memberRepository());
    }

    @Bean
    public UserRepository memberRepository() {
        return new MemoryUserRepoisitory();
    }


}
