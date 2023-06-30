package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
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
    public void updateUser(UserDto userDto){
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        String name = userDto.getName();

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
    }

    @Transactional
    public boolean emailDuplicateCheck(UserDto userDto){
        Optional<User> findEmail = userRepository.findByEmail(userDto.getEmail());
        if(findEmail.isPresent())
            return true;
        else
            return false;
    }

    @Transactional
    public String join(UserDto userDto){
        //중복된 아이디가 있는 지 검증
        Optional<User> findEmail = userRepository.findByEmail(userDto.getEmail());
        if (findEmail.isPresent()) {
            throw new IdDuplicateException("이미 존재하는 Id 입니다");
        } else {

            User user = User.builder()
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))  //비밀번호 인코딩
                    .name(userDto.getName())
                    .roles(Collections.singletonList("USER"))         //roles는 최초 USER로 설정
                    .build();

            return userRepository.save(user).getEmail();
        }

    }

    @Transactional
    public String login(UserDto userDto){
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        // 로그인에 성공하면 email, roles 로 토큰 생성 후 반환
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }
}
