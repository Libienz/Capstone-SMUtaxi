package com.capstone.smutaxi.config;

import com.capstone.smutaxi.entity.SystemMessage;
import com.capstone.smutaxi.entity.UserMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String servers;

    @Bean
    public ConsumerFactory<String, UserMessage> userMessageConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-id");

        JsonDeserializer<UserMessage> jsonDeserializer = new JsonDeserializer<>(UserMessage.class);
        jsonDeserializer.addTrustedPackages("com.capstone.smutaxi.entity");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new ErrorHandlingDeserializer<>(jsonDeserializer));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserMessage> userMessageKafkaListener() {
        ConcurrentKafkaListenerContainerFactory<String, UserMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SystemMessage> systemMessageConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-id");

        JsonDeserializer<SystemMessage> jsonDeserializer = new JsonDeserializer<>(SystemMessage.class);
        jsonDeserializer.addTrustedPackages("com.capstone.smutaxi.entity");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new ErrorHandlingDeserializer<>(jsonDeserializer));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SystemMessage> systemMessageKafkaListener() {
        ConcurrentKafkaListenerContainerFactory<String, SystemMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(systemMessageConsumerFactory());
        return factory;
    }
}