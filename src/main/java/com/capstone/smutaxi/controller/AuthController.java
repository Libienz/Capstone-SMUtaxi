package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/auth") //리소스 계층화
@RestController
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; //스프링 시큐리티 authentication manager
    private final JwtTokenProvider jwtTokenProvider;



    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * @ResponseBody
     * http 응답메시지의 body부에 return하는 내용을 직접 넣겠다.
     * html 파일을 동적으로 rendering하는 view Resolver가 아닌 HttpMessageConverter(JsonConverter 혹은 String Converter_가 호출된다.
     * 그 과정에서 스프링은 객체를 json으로 바꾸어주는 jackson 라이브러리를 이용한다.
     * 이로써 객체를 반환하면 json form으로 말아주고 String을 반환하면 http 메시지 바디부에 직접 써주는 것.
     */
    @GetMapping("login")
    @ResponseBody
    public String login(@RequestBody UserDto userDto) {
        return userService.login(userDto);
    }

    @PostMapping("join")
    @ResponseBody
    public String join(@Valid @RequestBody UserDto userDto) {
        return userService.join(userDto);
    }
}
