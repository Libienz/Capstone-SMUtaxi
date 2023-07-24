package com.capstone.smutaxi.service.auth;

import com.capstone.smutaxi.dto.responses.VerificationResponse;
import com.capstone.smutaxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    private static final String senderEmail= "oddman0w0@gmail.com";

    private static int mailAuthNumber;

    //메일 인증에 사용될 랜덤 숫자 생성
    public static void createNumber(){
        mailAuthNumber = ThreadLocalRandom.current().nextInt(10000, 100000);
    }

    //메일 message 작성
    public MimeMessage CreateMail(@RequestBody String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "SPOOT TAXI: 요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + mailAuthNumber + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    //작성한 message를 포함하는 메일 전송
    //foundThenSend = true: 가입된 이메일에 대해서만 인증번호 전송
    //foundThenSend = false: 가입되지 않은 이메일일 경우 인증번호 전송
    public VerificationResponse sendVerificationEmail(String email, boolean foundThenSend) {

        VerificationResponse verificationResponse;
        boolean present = userRepository.findByEmail(email).isPresent();

        //foundThenSend와 가입된 이메일 존재 여부 비교
        if ((present && foundThenSend) || (!present && !foundThenSend)) {
            //메일 생성, 전송
            MimeMessage message = CreateMail(email);
            javaMailSender.send(message);
            verificationResponse = VerificationResponse.builder().
                    verificationCode(mailAuthNumber).
                    sended(Boolean.TRUE).
                    build();
        } else {
            //메일 전송 안함 (foundThenSend와 상태가 맞지 않은 이유로)
            verificationResponse = VerificationResponse.builder()
                    .sended(Boolean.FALSE)
                    .build();
        }

        return verificationResponse;
    }

}
