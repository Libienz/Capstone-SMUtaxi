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

    public static void createNumber(){
        mailAuthNumber = ThreadLocalRandom.current().nextInt(10000, 100000);
    }

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

    public VerificationResponse sendVerificationEmail(String email, boolean foundThenSend){

        VerificationResponse verificationResponse;
        boolean present = userRepository.findByEmail(email).isPresent();

        if ((present && foundThenSend) || (!present && !foundThenSend)) { //가입된 아이디일때 전송 foundThenSend
            MimeMessage message = CreateMail(email);
            javaMailSender.send(message);
            verificationResponse = VerificationResponse.builder().
                    verificationCode(mailAuthNumber).
                    sended(Boolean.TRUE).
                    build();
        } else {
            verificationResponse = VerificationResponse.builder()
                    .sended(Boolean.FALSE)
                    .build();
        }

        return verificationResponse;
    }

}
