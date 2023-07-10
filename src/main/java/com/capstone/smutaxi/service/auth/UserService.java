package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.LoginResponse;
import com.capstone.smutaxi.dto.responses.UserSaveResponse;
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
    public UserSaveResponse updateUser(String email, UserDto updateDto){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        String password = updateDto.getPassword();
        String name = updateDto.getName();
        Gender gender = updateDto.getGender();
        String imgPath = updateDto.getImgUri();

        //유저 정보 수정
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setGender(gender);
        user.setImgPath(imgPath);

        userRepository.save(user); //이미 존재하면 업데이트 없으면 생성

        //응답 메시지 말기
        UserDto userDto = userToUserDto(user);
        userDto.setPassword(password);
        UserSaveResponse userSaveResponse = UserSaveResponse.builder()
                .success(Boolean.TRUE)
                .message(null)
                .userDto(userDto)
                .build();
        return userSaveResponse;
    }

    @Transactional
    public UserSaveResponse updateUserPassword(String email, UserDto updateDto) {
        //프론트에서 한번 검증 거쳐서 user가 null인 흐름은 없긴 함
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        //유저의 비밀번호만 수정
        String password = updateDto.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        //응답 메시지 말기
        UserDto userDto = userToUserDto(user);
        userDto.setPassword(password);
        UserSaveResponse userSaveResponse = UserSaveResponse.builder()
                .success(Boolean.TRUE)
                .message(null)
                .userDto(userDto)
                .build();
        return userSaveResponse;
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
    public String join(UserDto joinDto){
        //중복된 아이디가 있는 지 검증
        Optional<User> findEmail = userRepository.findByEmail(joinDto.getEmail());
        if (findEmail.isPresent()) {
            throw new IdDuplicateException("이미 존재하는 Id 입니다");
        } else {

            User user = User.builder()
                    .email(joinDto.getEmail())
                    .password(passwordEncoder.encode(joinDto.getPassword()))  //비밀번호 인코딩
                    .name(joinDto.getName())
                    .gender(joinDto.getGender())
                    .roles(Collections.singletonList("USER"))         //roles는 최초 USER로 설정
                    .build();

            return userRepository.save(user).getEmail();
        }

    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        //로그인 시도
        //참고 : BcryptEncoder는 단방향 해시임으로 서버도 해싱된걸 디코딩할 수 없다 -> 단 match해보고 패스워드가 정확한지 인증은 가능
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        // 응답 메시지 말기
        UserDto userDto = userToUserDto(user);
        LoginResponse loginResponse = LoginResponse
                .builder()
                .userDto(userDto)
                .error(null)
                .token(token)
                .build();
        return loginResponse;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto(user.getEmail(), user.getPassword(), user.getImgPath(), user.getName(), user.getGender());
        return userDto;
    }
}
