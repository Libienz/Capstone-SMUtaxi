package com.capstone.smutaxi.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;
    private final ObjectMapper objectMapper;
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/spoot-taxi/messages:send";

//    // fcm 기본 설정 진행
//    // Firebase에 Admin 계정을 인증하는 작업
//    @PostConstruct
//    public void init() {
//        try {
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(
//                            GoogleCredentials
//                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
//                                    .createScoped(List.of(fireBaseScope)))
//                    .build();
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//                log.info("Firebase application has been initialized");
//            }
//        } catch (java.io.IOException e) {
//            log.error(e.getMessage());
//            // spring 뜰때 알림 서버가 잘 동작하지 않는 것이므로 바로 죽임
//            throw new RuntimeException(e.getMessage());
//        }
//    }


    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
    // 알림 보내기
    public void sendByTokenList(List<String> tokenList) {

        // 메시지 만들기
        List<Message> messages = tokenList.stream().map(token -> Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .setNotification(new Notification("제목", "알림 내용"))
                .setToken(token)
                .build()).collect(Collectors.toList());

        // 요청에 대한 응답을 받을 response
        BatchResponse response;
        try {

            // 알림 발송
            response = FirebaseMessaging.getInstance().sendAll(messages);

            // 요청에 대한 응답 처리
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(tokenList.get(i));
                    }
                }
                log.error("List of tokens are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }
}
