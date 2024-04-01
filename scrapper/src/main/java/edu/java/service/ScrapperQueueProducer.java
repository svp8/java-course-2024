package edu.java.service;

import edu.java.dto.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, Update> kafkaTemplate;
    @Value("${app.kafka.topic}")
    private String topic;

    public void sendUpdate(Update update) {
        // TODO
        kafkaTemplate.send(topic, update);
    }
}
