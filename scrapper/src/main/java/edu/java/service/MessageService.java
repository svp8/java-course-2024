package edu.java.service;

import edu.java.client.BotClient;
import edu.java.dto.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class MessageService {
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final BotClient botClient;

    private final Boolean useQueue;

    public MessageService(
        ScrapperQueueProducer scrapperQueueProducer,
        BotClient botClient,
        @Value("${app.useQueue}") Boolean useQueue
    ) {
        this.scrapperQueueProducer = scrapperQueueProducer;
        this.botClient = botClient;
        this.useQueue = useQueue;
    }

    public void sendUpdate(Update update) {
        if (useQueue) {
            scrapperQueueProducer.sendUpdate(update);
        } else {
            botClient.sendUpdate(update);
        }
    }
}
