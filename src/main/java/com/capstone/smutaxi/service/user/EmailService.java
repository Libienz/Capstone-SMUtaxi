package com.capstone.smutaxi.service.user;

import com.capstone.smutaxi.dto.responses.auth.EmailVerificationResponse;
import com.capstone.smutaxi.dto.responses.ResponseFactory;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.exception.user.EmailFailToSendException;
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
            message.setSubject("Spoot Taxi Verification mail");
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
    public EmailVerificationResponse sendVerificationEmail(String email, boolean foundThenSend) {

        EmailVerificationResponse emailVerificationResponse;
        boolean present = userRepository.findById(email).isPresent();
        System.out.println("present = " + present);
        System.out.println("foundThenSend = " + foundThenSend);
        //foundThenSend와 가입된 이메일 존재 여부 비교
        if ((present && foundThenSend) || (!present && !foundThenSend)) {
            //메일 생성, 전송
            MimeMessage message = CreateMail(email);
            javaMailSender.send(message);
            return ResponseFactory.createEmailVerificationResponse(Boolean.TRUE, null, mailAuthNumber);
        }
        //메일 전송 안함 (foundThenSend와 상태가 맞지 않은 이유로)
        else {
            //비밀번호 수정을 위해 재학생 메일 인증을 요청했는데 가입되지 않은 경우
            if (!present) {
                throw new EmailFailToSendException(ErrorCode.USER_NOT_FOUND);
            }
            //회원가입을 위해 재학생 메일 인증을 요청했는데 이미 가입된 아이디일 경우
            else {
                throw new EmailFailToSendException(ErrorCode.USER_ALREADY_EXIST);
            }
        }

    }

}
