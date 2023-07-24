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

    //메세지를 저장
    @Transactional
    public void sendMessage(Message message){
        messageRepository.save(message);
    }
}
