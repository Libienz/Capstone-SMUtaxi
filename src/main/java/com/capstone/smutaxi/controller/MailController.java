package com.capstone.smutaxi.controller;

import com.capstone.smutaxi.dto.MailDto;
import com.capstone.smutaxi.service.auth.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/mail")
    public ResponseEntity<Integer> MailSend(@RequestParam String mail){
        int number = mailService.sendMail(mail);

        return ResponseEntity.ok().body(number);
    }
}
