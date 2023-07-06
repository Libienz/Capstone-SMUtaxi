package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.JoinDto;
import com.capstone.smutaxi.dto.LoginDto;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
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
    private final AuthenticationManager authenticationManager; //스프링 시큐리티 authentication manager
    private final JwtTokenProvider jwtTokenProvider;



    @Autowired
    public AuthController(EmailService emailService, UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.emailService = emailService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    //로그인 API
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            // 인증 수행 및 JWT 토큰 생성
            String jwtToken = userService.login(loginDto);
            // 로그인 성공 응답: JWT 토큰 반환
            return ResponseEntity.ok(jwtToken);
        } catch (IllegalArgumentException e) {
            // 로그인 실패 응답: 상태 코드와 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
        }
    }

    //회원가입 API
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<String> join(@Valid @RequestBody JoinDto joinDto) {
        try {
            String joinedEmail = userService.join(joinDto);
            return ResponseEntity.ok(joinedEmail);
        } catch (IdDuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }

    }

    //이메일 중복 확인 API
    @GetMapping("/check-duplicate/{email}")
    @ResponseBody
    public ResponseEntity<String> checkEmailDuplicate(@PathVariable("email") String email) {
        boolean emailDuplicate = userService.emailDuplicateCheck(email);
        if(emailDuplicate)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 있는 아이디입니다.");
        else
            return ResponseEntity.ok().body("생성가능한 아이디입니다.");
    }

    //인증메일 발송 API
    @PostMapping("/email-verification")
    public ResponseEntity<Integer> emailVerification(@RequestParam String email){
        int verificationNumber = emailService.sendVerificationEmail(email);
        return ResponseEntity.ok().body(verificationNumber);
    }

    //유저 업데이트 API
    @PutMapping("/users/{userEmail}")
    public ResponseEntity<String> updateUser(@RequestBody JoinDto updateDto){ //UpdateDto와 JoinDto 구조 같음 -> 재활용
        userService.updateUser(updateDto);
        return ResponseEntity.ok().body(updateDto.toString());
    }
}
