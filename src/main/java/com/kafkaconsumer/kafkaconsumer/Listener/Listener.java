package com.kafkaconsumer.kafkaconsumer.Listener;

import com.kafkaconsumer.kafkaconsumer.model.Users;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Listener {


    @KafkaListener(topics = "UserExample2", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(Users Users){
        System.err.println("Consumed "+ Users);
    }
    
}