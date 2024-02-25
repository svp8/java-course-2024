package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class TrackCommand extends Command {

    private final ScrapperClient scrapperClient;

    public TrackCommand(Bot bot, ScrapperClient scrapperClient) {
        super(CommandType.TRACK, bot);
        this.scrapperClient = scrapperClient;
    }

    @Override
    public void execute(Update update, boolean isInDialog) {
        if (!isInDialog) {
            Long chatId = update.message().chat().id();
            if (update.message().text().length() <= CommandType.TRACK.getName().length()) {
                super.getBot().sendMessage(chatId, "Format for command: /track {your link}");
                return;
            }
            String uri = update.message()
                .text()
                .substring(CommandType.TRACK.getName().length() + 1)
                .trim();

            Link link = scrapperClient.trackLink(chatId, uri);
            super.getBot().sendMessage(chatId, "tracking " + link.getUri().toString());

        }

    }
}
