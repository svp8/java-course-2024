package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.scrapper.LinkUpdate;
import edu.java.bot.model.scrapper.Update;
import java.util.List;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
    private final Bot bot;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public UpdateService(Bot bot, KafkaTemplate<String, String> kafkaTemplate) {
        this.bot = bot;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${app.kafka.topic}")
    public void sendUpdate(Update update) {
        List<LinkUpdate> linkUpdates = update.linkUpdates();
        SendResponse sendResponse =
            bot.sendMessage(update.chat().id(), formatUpdates(update.link(), linkUpdates));
        if (!sendResponse.isOk()) {
            kafkaTemplate.send("dlq", sendResponse.description());
            throw new SendMessageException(sendResponse.errorCode(), sendResponse.description());
        }
    }

    public String formatUpdates(Link link, List<LinkUpdate> linkUpdates) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(link.getUri().toString());
        stringBuilder.append("\n");
        for (LinkUpdate linkUpdate : linkUpdates) {
            stringBuilder
                .append(linkUpdate.description()).append("\n");
        }
        return stringBuilder.toString();
    }
}
