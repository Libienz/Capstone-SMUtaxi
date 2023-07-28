package com.capstone.smutaxi.service;

import com.capstone.smutaxi.entity.ChatRoom;
import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.exception.ChatRoomNotFoundException;
import com.capstone.smutaxi.exception.ErrorCode;
import com.capstone.smutaxi.repository.ChatRoomRepository;
import com.capstone.smutaxi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    //메세지를 저장
    @Transactional
    public void saveMessage(Message message){
        Long chatRoomId = message.getChatRoom().getId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException(ErrorCode.CHATROOM_NOT_FOUND));

        List<Message> messageList = chatRoom.getMessageList();
        messageList.add(message);
        //ChatRoom은 Message와 cascade로 연결되어있기 때문에 부모인 ChatRoom을 업데이트하면 Message도 자동으로 저장된다.
        //그래서 ChatRoom을 save하려했는데 '변경감지' 기능때문에 명시적으로 save메서드를 호출안해도 업데이트 되고 Message도 저장되는 모습
        //chatRoomRepository.save(chatRoom);

    }
}
