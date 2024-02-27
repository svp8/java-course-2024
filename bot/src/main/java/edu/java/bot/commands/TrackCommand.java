package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class TrackCommand extends Command {

    public static final String FORMAT = "Format for command: /track {your link}";
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
                super.getBot().sendMessage(chatId, FORMAT);
                return;
            }
            String uri = update.message()
                .text()
                .substring(CommandType.TRACK.getName().length() + 1)
                .trim();
            try{
                Link link = scrapperClient.trackLink(chatId, uri);
                super.getBot().sendMessage(chatId, "tracking " + link.getUri().toString());
            }catch(WebClientResponseException e){
                ScrapperException scrapperException =e.getResponseBodyAs(ScrapperException.class);
                System.out.println(scrapperException.getDescription());
                super.getBot().sendMessage(chatId, scrapperException.getDescription());
            }
        }

    }
}
