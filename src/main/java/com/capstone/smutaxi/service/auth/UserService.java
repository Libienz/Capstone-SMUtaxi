package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.requests.JoinRequest;
import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.enums.Gender;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
import com.capstone.smutaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }
    @Transactional
    public void updateUser(JoinRequest updateDto){
        User user = userRepository.findByEmail(updateDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        String password = updateDto.getPassword();
        String name = updateDto.getName();
        Gender gender = updateDto.getGender();


        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setGender(gender);
    }

    @Transactional
    public boolean emailDuplicateCheck(String email){
        Optional<User> findEmail = userRepository.findByEmail(email);
        if(findEmail.isPresent())
            return true;
        else
            return false;
    }

    @Transactional
    public String join(JoinRequest joinRequest){
        //중복된 아이디가 있는 지 검증
        Optional<User> findEmail = userRepository.findByEmail(joinRequest.getEmail());
        if (findEmail.isPresent()) {
            throw new IdDuplicateException("이미 존재하는 Id 입니다");
        } else {

            User user = User.builder()
                    .email(joinRequest.getEmail())
                    .password(passwordEncoder.encode(joinRequest.getPassword()))  //비밀번호 인코딩
                    .name(joinRequest.getName())
                    .gender(joinRequest.getGender())
                    .roles(Collections.singletonList("USER"))         //roles는 최초 USER로 설정
                    .build();

            return userRepository.save(user).getEmail();
        }

    }

    @Transactional
    public User login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return user;
    }
}
