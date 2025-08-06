package com.example.schoolmanagement.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "school-events", groupId = "school-group")
    public void consume(String message) {
        System.out.println("Received Kafka message: " + message);
        // Handle the message: e.g., log, trigger email, update audit logs, etc.
    }
}
