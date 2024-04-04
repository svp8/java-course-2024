package edu.java.bot.service;

import edu.java.bot.model.Bot;
import edu.java.bot.model.scrapper.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaUpdateService {

    private final Bot bot;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UpdateService updateService;

    public KafkaUpdateService(Bot bot, KafkaTemplate<String, String> kafkaTemplate, UpdateService updateService) {
        this.bot = bot;
        this.kafkaTemplate = kafkaTemplate;
        this.updateService = updateService;
    }

    @KafkaListener(topics = "${app.kafka.topic}")
    public void sendUpdate(Update update) {
        try {
            updateService.sendUpdate(update);
        } catch (Exception e) {
            kafkaTemplate.send("dlq", e.toString());
        }

    }
}
