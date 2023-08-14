package com.capstone.smutaxi.service;

import com.capstone.smutaxi.entity.SystemMessage;
import com.capstone.smutaxi.entity.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class KafkaConsumerService {
    private final MessageService messageService;

    @KafkaListener(topics = "user-message-topic", groupId = "consumer-id", containerFactory = "userMessageKafkaListener")
    public void userMessageConsume(UserMessage message){
        messageService.saveMessage(message);
    }
    @KafkaListener(topics = "system-message-topic", groupId = "consumer-id", containerFactory = "systemMessageKafkaListener")
    public void systemMessageConsume(SystemMessage message){
        messageService.saveMessage(message);
    }

}