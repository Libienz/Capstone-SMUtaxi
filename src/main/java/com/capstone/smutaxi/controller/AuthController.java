package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.*;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
import com.capstone.smutaxi.service.auth.AuthService;
import com.capstone.smutaxi.service.auth.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;
    private final AuthService authService;


    //로그인 API
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 인증 수행 및 JWT 토큰 생성
            LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException e) {
            // 로그인 실패 응답 build
            LoginResponse errorResponse = ResponseFactory.createLoginResponse(Boolean.FALSE, e.toString(), null, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    //회원가입 API
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<JoinResponse> join(@Valid @RequestBody UserDto joinDto) {
        try {
            JoinResponse joinResponse = authService.join(joinDto);
            return ResponseEntity.ok(joinResponse);
        } catch (IdDuplicateException e) {
            // 회원가입 실패 응답 build
            JoinResponse errorResponse = ResponseFactory.createJoinResponse(Boolean.FALSE, e.toString(), null, null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

    }

    //이메일 중복 확인 API
    @GetMapping("/check-duplicate/{email}")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable("email") String email) {
        boolean emailDuplicate = authService.emailDuplicateCheck(email);
        if(emailDuplicate)
            return ResponseEntity.ok().body(Boolean.TRUE);
        else
            return ResponseEntity.ok().body(Boolean.FALSE);
    }

    //재학생 인증메일 발송 API (회원가입 시)
    @GetMapping("/join/email-verification")
    public ResponseEntity<EmailVerificationResponse> emailVerificationForJoin(@RequestParam String email){
        try {
            EmailVerificationResponse emailVerificationResponse = emailService.sendVerificationEmail(email, false);
            return ResponseEntity.ok().body(emailVerificationResponse);
        } catch (IllegalArgumentException e) {
            EmailVerificationResponse errorResponse = ResponseFactory.createEmailVerificationResponse(Boolean.FALSE, e.toString(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }
    //비밀번호 수정 인증메일 발송 API
    @GetMapping("/update/email-verification")
    public ResponseEntity<EmailVerificationResponse> emailVerificationForUpdate(@RequestParam String email){
        try {
            EmailVerificationResponse emailVerificationResponse = emailService.sendVerificationEmail(email, true);
            return ResponseEntity.ok().body(emailVerificationResponse);
        } catch (IllegalArgumentException e) {
            EmailVerificationResponse errorResponse = ResponseFactory.createEmailVerificationResponse(Boolean.FALSE, e.toString(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }



}
