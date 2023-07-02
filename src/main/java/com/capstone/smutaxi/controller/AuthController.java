package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.exception.auth.IdDuplicateException;
import com.capstone.smutaxi.service.auth.MailService;
import com.capstone.smutaxi.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/auth") //리소스 계층화
@RestController
public class AuthController {

    private final MailService mailService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager; //스프링 시큐리티 authentication manager
    private final JwtTokenProvider jwtTokenProvider;



    @Autowired
    public AuthController(MailService mailService, UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.mailService = mailService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * @ResponseBody http 응답메시지의 body부에 return하는 내용을 직접 넣겠다.
     * html 파일을 동적으로 rendering하는 view Resolver가 아닌 HttpMessageConverter(JsonConverter 혹은 String Converter_가 호출된다.
     * 그 과정에서 스프링은 객체를 json으로 바꾸어주는 jackson 라이브러리를 이용한다.
     * 이로써 객체를 반환하면 json form으로 말아주고 String을 반환하면 http 메시지 바디부에 직접 써주는 것.
     */
    @GetMapping("login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {

        try {
            //인증 수행 및 jwt 토큰 생성
            String jwtToken = userService.login(userDto);
            //성공 응답 반환
            return ResponseEntity.ok(jwtToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
        }

    }

    @PostMapping("join")
    @ResponseBody
    public ResponseEntity<String> join(@Valid @RequestBody UserDto userDto) {
        try {
            String joinedEmail = userService.join(userDto);
            return ResponseEntity.ok(joinedEmail);
        } catch (IdDuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }

    }

    @GetMapping("idCheck")
    @ResponseBody
    public ResponseEntity<String> idCheck(@RequestBody UserDto userDto) {
        boolean emailDuplicateCheck = userService.emailDuplicateCheck(userDto);
        if(emailDuplicateCheck)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 있는 아이디입니다.");
        else
            return ResponseEntity.ok().body("생성가능한 아이디입니다.");
    }

    @PostMapping("/mail")
    public ResponseEntity<Integer> MailSend(@RequestParam String mail){
        int number = mailService.sendMail(mail);

        return ResponseEntity.ok().body(number);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDto){
        userService.updateUser(userDto);
        return ResponseEntity.ok().body(userDto.toString());
    }
}
