package com.capstone.smutaxi.chat;

import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.MessageService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChattingTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    MessageService messageService;


    /**
     * chotRoom을 만들고 해당 id가 같은지 확인
     */
    @Test
    public void makeChatRoom(){
        ChatRoom chatRoom1 = chatRoomService.createChatRoom("chatRoom1");
        Long chatRoom1Id = chatRoom1.getId();

        Optional<ChatRoom> byId = chatRoomRepository.findById(chatRoom1Id);

        Assertions.assertThat(byId.get().getId()).isEqualTo(chatRoom1.getId());

    }

}
