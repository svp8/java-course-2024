package edu.java.service;

import edu.java.dto.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, Update> kafkaTemplate;

    private final String topic;

    public ScrapperQueueProducer(
        KafkaTemplate<String, Update> kafkaTemplate,
        @Value("${app.kafka.topic}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendUpdate(Update update) {
        kafkaTemplate.send(topic, update);
    }
}
