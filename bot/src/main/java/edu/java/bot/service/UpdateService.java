package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.scrapper.LinkUpdate;
import edu.java.bot.model.scrapper.Update;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
    private final Bot bot;

    public UpdateService(Bot bot) {
        this.bot = bot;
    }

    public void sendUpdate(Update update) {
        List<LinkUpdate> linkUpdates = update.linkUpdates();
        SendResponse sendResponse =
            bot.sendMessage(update.chat().id(), formatUpdates(update.link(), linkUpdates));
        if (!sendResponse.isOk()) {
            throw new SendMessageException(sendResponse.errorCode(), sendResponse.description());
        }

    }

    public static String formatUpdates(Link link, List<LinkUpdate> linkUpdates) {
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
