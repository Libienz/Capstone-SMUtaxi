package com.capstone.smutaxi.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasicUserService implements UserService {


    private final UserRepository userRepository;

    @Autowired
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원가입
     *
     */
    @Override
    public void join(User user) {
        //같은 id 안됨
        validateDuplicateMember(user);
        userRepository.save(user);
    }

    @Override
    public boolean login(String id, String pw) {
        return false;
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }

    /**
     * 전체 회원 조회
     *
     */
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(String Email) {
        return userRepository.findByEmail(Email);
    }





}
