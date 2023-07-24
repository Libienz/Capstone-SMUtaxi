package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.JoinResponse;
import com.capstone.smutaxi.dto.responses.LoginResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.enums.Role;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    //회원가입
    @Transactional
    public JoinResponse join(UserDto joinDto) {
        //중복된 아이디가 있는 지 검증
        Optional<User> findEmail = userRepository.findByEmail(joinDto.getEmail());
        if (findEmail.isPresent()) {
            throw new IdDuplicateException("이미 존재하는 Id 입니다");
        }
        //유저 초기화
        User user = User.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .imageUrl(joinDto.getImgUrl())
                .name(joinDto.getName())
                .gender(joinDto.getGender())
                .roles(Collections.singletonList(Role.USER.getRoleName()))
                .build();
        //회원가입
        userRepository.save(user);

        //Response Dto 생성 -> {success, message, userDto, token}
        UserDto userDto = user.userToUserDto();
        userDto.setPassword(joinDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        JoinResponse joinResponse = ResponseFactory.createJoinResponse(Boolean.TRUE, null, userDto, token);
        return joinResponse;

    }

    //로그인
    public LoginResponse login(LoginRequest loginRequest) {

        //가입되지 않은 이메일인지 체크
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        //로그인 시도 (참고 : BcryptEncoder는 단방향 해시임으로 서버도 해싱된걸 디코딩할 수 없다 -> 단 match는 가능)
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        //Response Dto 생성 -> {success, message, userDto, token}
        UserDto userDto = user.userToUserDto();
        userDto.setPassword(loginRequest.getPassword());
        LoginResponse loginResponse = ResponseFactory.createLoginResponse(Boolean.TRUE, null, userDto, token);
        return loginResponse;
    }
}
