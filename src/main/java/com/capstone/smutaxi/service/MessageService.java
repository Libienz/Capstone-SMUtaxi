package com.capstone.smutaxi.service;

import com.capstone.smutaxi.entity.Message;
import com.capstone.smutaxi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;

    //원본에는 dto로 해서 builder로 하던데 왜그렇지? 일단 안쓰고 진행
    //dto로 안하니까 send할때 Message를 그대로쓰는데 그러면 json으로 보낼때 User 같은거 만들때 골치아픈듯?
    @Transactional //모든예외에 대해서 롤백?
    public void sendMessage(Message message){
        messageRepository.save(message);

    }
}
