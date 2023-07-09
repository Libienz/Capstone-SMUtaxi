package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.requests.JoinRequest;
import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.LoginResponse;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.VerificationResponse;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.auth.EmailService;
import com.capstone.smutaxi.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final EmailService emailService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager; //스프링 시큐리티 authentication manager
    private final JwtTokenProvider jwtTokenProvider;



    @Autowired
    public AuthController(EmailService emailService, UserService userService, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.emailService = emailService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }



    //로그인 API
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 인증 수행 및 JWT 토큰 생성
            User user = userService.login(loginRequest);
            String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

            // 로그인에 성공하면 전송용 객체인 유저 DTO 만다 (직렬화 위해)
            UserDto userDto = UserDto.builder().
                    email(user.getEmail()).
                    password(loginRequest.getPassword()).
                    gender(user.getGender()).
                    imgUri(/*user.getImgPath()*/ null).
                    name(user.getName()).
                    build();
            // 로그인 성공 응답 build
            LoginResponse response = LoginResponse.builder().
                    token(token).
                    userDto(userDto).
                    error(null)
                    .build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 로그인 실패 응답 build
            LoginResponse errorResponse = LoginResponse.builder().
                    error(e.toString()).
                    build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    //회원가입 API
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequest joinRequest) {
        try {
            String joinedEmail = userService.join(joinRequest);
            return ResponseEntity.ok(joinedEmail);
        } catch (IdDuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }

    }

    //이메일 중복 확인 API
    @GetMapping("/check-duplicate/{email}")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable("email") String email) {
        boolean emailDuplicate = userService.emailDuplicateCheck(email);
        if(emailDuplicate)
            return ResponseEntity.ok().body(Boolean.TRUE);
        else
            return ResponseEntity.ok().body(Boolean.FALSE);
    }

    //인증메일 발송 API
    @GetMapping("/email-verification")
    public ResponseEntity<VerificationResponse> emailVerification(@RequestParam String email){
        VerificationResponse verificationResponse = emailService.sendVerificationEmail(email);
        return ResponseEntity.ok().body(verificationResponse);
    }

    //유저 업데이트 API
    @PutMapping("/users/{userEmail}")
    public ResponseEntity<String> updateUser(@RequestBody JoinRequest updateDto){ //UpdateDto와 JoinRequest 구조 같음 -> 재활용
        userService.updateUser(updateDto);
        return ResponseEntity.ok().body(updateDto.toString());
    }
}
