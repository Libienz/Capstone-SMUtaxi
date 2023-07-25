package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.requests.LoginRequest;
import com.capstone.smutaxi.dto.responses.*;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.service.user.AuthService;
import com.capstone.smutaxi.service.user.EmailService;
import lombok.RequiredArgsConstructor;
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
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    //회원가입 API
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<JoinResponse> join(@Valid @RequestBody UserDto joinDto) {
        JoinResponse joinResponse = authService.join(joinDto);
        return ResponseEntity.ok(joinResponse);
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

    //재학생 인증메일 발송 API
    @GetMapping("/send/verification-email")
    public ResponseEntity<EmailVerificationResponse> emailVerificationForJoin(@RequestParam String email, @RequestParam boolean foundThenSend){
        EmailVerificationResponse emailVerificationResponse = emailService.sendVerificationEmail(email, foundThenSend);
        return ResponseEntity.ok().body(emailVerificationResponse);
    }



}
