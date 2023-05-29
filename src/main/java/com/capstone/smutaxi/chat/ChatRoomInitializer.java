package com.capstone.smutaxi.chat;

import com.capstone.smutaxi.chat.domain.ChatRoom;
import com.capstone.smutaxi.chat.domain.GenderRestriction;
import com.capstone.smutaxi.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 이 클래스는 스프링 부트 애플리케이션이 시작될때 run 메서드가 실행되는데
 * 쓰레드 풀처럼 미리 300개의 ChatRoom을 만들어놓는 클래스
 * ChatRoom1 ~ 100까지는 F_ONLY
 * ChatRoom100 ~ 200까지는 M_ONLY
 * ChatRoom200 ~ 300까지는 ANY
 */
@RequiredArgsConstructor
@Component
public class ChatRoomInitializer implements ApplicationRunner {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createPredefinedChatRooms();
    }

    private void createPredefinedChatRooms() {
        for (int i = 1; i <= 300; i++) {
            String chatRoomName = "ChatRoom " + i;
            GenderRestriction genderRestriction;

            if (i <= 100) {
                genderRestriction = GenderRestriction.F_ONLY;
            } else if (i <= 200) {
                genderRestriction = GenderRestriction.M_ONLY;
            } else {
                genderRestriction = GenderRestriction.ANY;
            }

            ChatRoom chatRoom = ChatRoom.create(chatRoomName, genderRestriction);
            chatRoomRepository.save(chatRoom);
        }
    }
}

