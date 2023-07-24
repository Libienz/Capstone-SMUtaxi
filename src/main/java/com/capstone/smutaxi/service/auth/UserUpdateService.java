package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.dto.responses.UserUpdateResponse;
import com.capstone.smutaxi.enums.Gender;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.utils.FileNameGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        String password = updateDto.getPassword();
        String name = updateDto.getName();
        Gender gender = updateDto.getGender();
        String imageUrl = updateDto.getImgUrl();

        //유저 정보 수정
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setGender(gender);
        user.setImageUrl(imageUrl);

        //업데이트
        userRepository.save(user);

        //Response Dto 생성
        UserDto userDto = user.userToUserDto();
        userDto.setPassword(password);
        return ResponseFactory.createUserUpdateResponse(Boolean.TRUE, null, userDto);

    }

    //유저 비밀번호 업데이트
    @Transactional
    public UserUpdateResponse updateUserPassword(String email, UserDto updateDto) {
        //프론트에서 한번 검증 거쳐서 user가 null인 흐름은 없긴 함
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        //유저의 비밀번호만 수정
        String password = updateDto.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        //Response Dto 생성

        UserDto userDto = user.userToUserDto();
        userDto.setPassword(password);
        return ResponseFactory.createUserUpdateResponse(Boolean.TRUE, null, userDto);

    }

}
