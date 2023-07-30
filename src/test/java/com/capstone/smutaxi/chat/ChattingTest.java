package com.capstone.smutaxi.chat;

import com.capstone.smutaxi.dto.UserDto;
import com.capstone.smutaxi.entity.ChatParticipant;
import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.User;
import com.capstone.smutaxi.enums.Gender;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.repository.UserRepository;
import com.capstone.smutaxi.service.ChatRoomService;
import com.capstone.smutaxi.service.MessageService;
import com.capstone.smutaxi.service.user.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChattingTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;


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

    @Rollback(value = false)
    @Test
    public void 채팅방_유저_추가(){

        //given
        ChatRoom chatRoom1 = chatRoomService.createChatRoom("chatRoom1");
        Long chatRoom1Id = chatRoom1.getId();

        UserDto user = new UserDto("test@sangmyung.kr","1234",null,"kim", Gender.MALE);
        authService.join(user);

        User getUser = userRepository.findByEmail("test@sangmyung.kr").get();
        ChatRoom getChatRoom = chatRoomRepository.findById(chatRoom1Id).get();

        //when
        Long addUserToChatRoom = chatRoomService.addUserToChatRoom(getChatRoom.getId(), getUser.getEmail());

        //then
        Long userChatParticipantId = null;
        Long chatRoomChatParticipantId;

        //이곳에서 null 오류가 나는 이유 해결 못함...
        List<ChatParticipant> chatParticipantList = getUser.getChatParticipantList();

        for (ChatParticipant chatParticipant : chatParticipantList) {
            userChatParticipantId =chatParticipant.getId();
        }
        Assert.assertEquals(addUserToChatRoom,userChatParticipantId);
    }

}
