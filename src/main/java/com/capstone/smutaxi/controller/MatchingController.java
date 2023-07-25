package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.config.jwt.JwtTokenProvider;
import com.capstone.smutaxi.dto.requests.MatchingRequest;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.matching.MatchingDispatcher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/match")
@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingDispatcher matchingDispatcher;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @ResponseBody
    @PostMapping("/request")
    public String matching(HttpServletRequest request, MatchingRequest matchingRequest) {

        //헤더로부터 토큰 받아와서 유저 식별
        String token = jwtTokenProvider.resolveToken(request);
        String userPk = jwtTokenProvider.getUserPk(token);
        User user = userRepository.findByEmail(userPk).get();
        Logger logger = LoggerFactory.getLogger(MatchingController.class);
        logger.info("libienz: " + user.toString());
        //매칭 요청
        Long chatRoomId = matchingDispatcher.handleMatchingRequest(user.getEmail(), matchingRequest);


        //채팅방 ID를 반환 -> 후에 클라이언트는 chatRoomID를 구독하는 요청을 보내게 됨
        return chatRoomId.toString();
    }

}
