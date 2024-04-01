package edu.java.service;

import edu.java.client.BotClient;
import edu.java.dto.Update;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final BotClient botClient;
    @Value("app.useQueue")
    private Boolean useQueue;

    public void sendUpdate(Update update) {
        if (useQueue) {
            scrapperQueueProducer.sendUpdate(update);
        } else {
            botClient.sendUpdate(update);
        }
    }
}
