package com.capstone.smutaxi.service.user;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.dto.responses.user.UserUpdateResponse;
import com.capstone.smutaxi.enums.Gender;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.user.UserNotFoundException;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserUpdateService {
    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    //유저 업데이트
    @Transactional
    public UserUpdateResponse updateUser(String email, UserDto updateDto){

        User user = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        String password = updateDto.getPassword();
        String name = updateDto.getName();
        Gender gender = updateDto.getGender();
        String imageUrl = updateDto.getImgUrl();

        //유저 정보 수정
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setGender(gender);
        user.setImageUrl(imageUrl);

        //Response Dto 생성
        UserDto userDto = user.userToUserDto();
        userDto.setPassword(password);
        return ResponseFactory.createUserUpdateResponse(Boolean.TRUE, null, userDto);

    }

    //유저 비밀번호 업데이트
    @Transactional
    public UserUpdateResponse updateUserPassword(String email, UserDto updateDto) {
        //프론트에서 한번 검증 거쳐서 user가 null인 흐름은 없긴 함
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        //유저의 비밀번호만 수정
        String password = updateDto.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        //Response Dto 생성

        UserDto userDto = user.userToUserDto();
        userDto.setPassword(password);
        return ResponseFactory.createUserUpdateResponse(Boolean.TRUE, null, userDto);

    }

}
