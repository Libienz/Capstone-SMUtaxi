package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
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
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${server.upload.directory}")
    private String uploadDirectory;
    private String serverDomain = "http://localhost:8080/";

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
        UserUpdateResponse userUpdateResponse = UserUpdateResponse.builder()
                .success(Boolean.TRUE)
                .message(null)
                .userDto(userDto)
                .build();
        return userUpdateResponse;
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
        UserUpdateResponse userUpdateResponse = UserUpdateResponse.builder()
                .success(Boolean.TRUE)
                .message(null)
                .userDto(userDto)
                .build();
        return userUpdateResponse;
    }

    //이메일 중복 확인
    public boolean emailDuplicateCheck(String email){
        Optional<User> findEmail = userRepository.findByEmail(email);
        if(findEmail.isPresent())
            return true;
        else
            return false;
    }





    //유저 프로필사진 업로드 후 img url 반환
    public String updateUserProfileImage(MultipartFile file) throws IOException {
        // 저장할 파일 이름 생성
        String fileName = FileNameGenerator.generateFileName();

        // 파일 저장 경로 생성
        String filePath = uploadDirectory + "/" + fileName;

        // 파일 저장
        File dest = new File(filePath);
        file.transferTo(dest);

        // URL 생성
        String imageUrl = serverDomain + "api/images/" + fileName;
        return imageUrl;
    }



        //프로필 이미지 리소스 가져오기
        public Resource getProfileImage(String fileName) throws MalformedURLException, FileNotFoundException {
            String imagePath = uploadDirectory + "/" + fileName;

            // 이미지 파일을 읽어옴
            Resource imageResource = new UrlResource("file:" + imagePath);

            // 파일이 존재하지 않는 경우
            if (!imageResource.exists()) {
                throw new FileNotFoundException("이미지를 찾을 수 없습니다.");
            }
            return imageResource;
        }

    //프로필 이미지 리소스의 미디어 타입 확인
    public MediaType getImageMediaType(String fileName) {
        // 이미지 파일 확장자에 따라 적절한 콘텐츠 타입 설정
        String fileExtension = FilenameUtils.getExtension(fileName);
        if (fileExtension.equalsIgnoreCase("png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("jpg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileExtension.equalsIgnoreCase("gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }



}
