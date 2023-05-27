package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.MatchingRequestDto;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.matching.MatchingDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MatchingController {

    private MatchingDispatcher matchingDispatcher;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    @Autowired
    public MatchingController(MatchingDispatcher matchingDispatcher, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.matchingDispatcher = matchingDispatcher;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @ResponseBody
    @PostMapping("/matching")
    public String matching(HttpServletRequest request, MatchingRequestDto matchingRequestDto) {

        //헤더로부터 토큰 받아와서 유저 식별
        String token = jwtTokenProvider.resolveToken(request);
        String userPk = jwtTokenProvider.getUserPk(token);
        User user = userRepository.findByEmail(userPk).get();
        Logger logger = LoggerFactory.getLogger(MatchingController.class);
        logger.info("libienz: " + user.toString());
        //매칭 요청
        matchingDispatcher.handleMatchingRequest(user.getEmail(), matchingRequestDto);

        //매칭 성공 시 채팅방 참여 혹은 생성으로 redirect
        return "libienz";
    }

}
