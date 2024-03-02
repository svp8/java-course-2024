package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.request.UpdateRequest;
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

    public void sendUpdates(UpdateRequest updateRequest) {
        for (Update update : updateRequest.updates()) {
            List<LinkUpdate> linkUpdates = update.linkUpdates();
            SendResponse sendResponse = bot.sendMessage(update.chatId(), formatUpdates(linkUpdates));
            if (!sendResponse.isOk()) {
                throw new SendMessageException(sendResponse.errorCode(), sendResponse.description());
            }
        }
    }

    public String formatUpdates(List<LinkUpdate> linkUpdates) {
        StringBuilder stringBuilder = new StringBuilder();
        for (LinkUpdate linkUpdate : linkUpdates) {
            stringBuilder.append(linkUpdate.link())
                .append(" - ")
                .append(linkUpdate.description());

        }
        return stringBuilder.toString();
    }
}
