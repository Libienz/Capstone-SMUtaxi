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

    private void validateDuplicateMember(User user) {
        userRepository.findById(user.getId())
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

    public Optional<User> findOne(String memberId) {
        return userRepository.findById(memberId);
    }


    @Override
    public boolean login(String id, String pw) {
        Optional<User> user = userRepository.findById(id);
        if (user == null){ //아이디 존재하지 않음
            return false;
        } else{
            if (pw.equals(user.get().getPassword())) {
                return true;
            } else {
                return false; //비밀번호 틀림
            }
        }

    }
}
