package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.JoinResponse;
import com.capstone.smutaxi.dto.responses.LoginResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.dto.responses.UserUpdateResponse;
import com.capstone.smutaxi.enums.Gender;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.enums.Role;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.utils.FileNameGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collections;
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

        userRepository.save(user); //이미 존재하면 업데이트 없으면 생성

        //응답 메시지 말기
        UserDto userDto = userToUserDto(user);
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

        //응답 메시지 말기
        UserDto userDto = userToUserDto(user);
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

    //회원가입
    @Transactional
    public JoinResponse join(UserDto joinDto){
        //중복된 아이디가 있는 지 검증
        Optional<User> findEmail = userRepository.findByEmail(joinDto.getEmail());
        if (findEmail.isPresent()) {
            throw new IdDuplicateException("이미 존재하는 Id 입니다");
        } else {

            User user = User.builder()
                    .email(joinDto.getEmail())
                    .password(passwordEncoder.encode(joinDto.getPassword()))  //비밀번호 인코딩
                    .imageUrl(joinDto.getImgUrl())
                    .name(joinDto.getName())
                    .gender(joinDto.getGender())
                    .roles(Collections.singletonList(Role.ADMIN.getRoleName()))         //roles는 최초 USER로 설정
                    .build();

            userRepository.save(user);
            UserDto userDto = userToUserDto(user);
            userDto.setPassword(joinDto.getPassword());
            String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

            JoinResponse joinResponse = JoinResponse.builder()
                    .token(token)
                    .userDto(userDto)
                    .build();
            return joinResponse;
        }

    }

    //로그인
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
        LoginResponse loginSuccessResponse = ResponseFactory.createLoginSuccessResponse(user, token);
        return loginSuccessResponse;
    }

    //user에서 필요한 정보만 추려 전송용 객체 생성
    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto(user.getEmail(), user.getPassword(), user.getImageUrl(), user.getName(), user.getGender());
        return userDto;
    }


    //유저 프로필사진 업로드 후 img url 반환
    public String updateUserProfileImage(MultipartFile file) throws IOException {
        // 저장할 파일 이름 생성
        String fileName = FileNameGenerator.generateFileName(); //email

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
