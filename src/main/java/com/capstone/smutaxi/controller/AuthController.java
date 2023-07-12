package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.JoinResponse;
import com.capstone.smutaxi.dto.responses.LoginResponse;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.dto.responses.UserUpdateResponse;
import com.capstone.smutaxi.dto.responses.VerificationResponse;
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
            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
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
    public ResponseEntity<JoinResponse> join(@Valid @RequestBody UserDto joinDto) {
        try {
            JoinResponse joinResponse = userService.join(joinDto);
            return ResponseEntity.ok(joinResponse);
        } catch (IdDuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
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
    @GetMapping("/join/email-verification")
    public ResponseEntity<VerificationResponse> emailVerificationForJoin(@RequestParam String email){
        VerificationResponse verificationResponse = emailService.sendVerificationEmail(email, false);
        return ResponseEntity.ok().body(verificationResponse);
    }
    @GetMapping("/update/email-verification")
    public ResponseEntity<VerificationResponse> emailVerificationForUpdate(@RequestParam String email){
        VerificationResponse verificationResponse = emailService.sendVerificationEmail(email, true);
        return ResponseEntity.ok().body(verificationResponse);
    }
    //유저 업데이트 API
    @PutMapping("/users/{email}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable("email") String email, @RequestBody UserDto updateDto) {

        try {
            UserUpdateResponse userUpdateResponse = userService.updateUser(email, updateDto);
            return ResponseEntity.ok().body(userUpdateResponse);
        } catch (IllegalArgumentException e) {
            UserUpdateResponse userUpdateResponse = UserUpdateResponse.builder()
                    .success(Boolean.FALSE)
                    .message(e.toString())
                    .build();
            return ResponseEntity.ok().body(userUpdateResponse);
        }


    }

    //유저 비밀번호 변경 API
    @PutMapping("/users/{email}/password")
    public ResponseEntity<UserUpdateResponse> updateUserPassword(@PathVariable("email") String email, @RequestBody UserDto updateDto) {

        try {
            UserUpdateResponse userUpdateResponse = userService.updateUserPassword(email, updateDto);
            return ResponseEntity.ok().body(userUpdateResponse);
        } catch (IllegalArgumentException e) {
            UserUpdateResponse userUpdateResponse = UserUpdateResponse.builder()
                    .success(Boolean.FALSE)
                    .message(e.toString())
                    .build();
            return ResponseEntity.ok().body(userUpdateResponse);
        }
    }


}
