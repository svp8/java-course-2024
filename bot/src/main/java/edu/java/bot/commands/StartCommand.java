package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class StartCommand extends Command {
    public static final String START_MESSAGE = "New user registered";
    private final ScrapperClient scrapperClient;

    protected StartCommand(Bot bot, ScrapperClient scrapperClient) {
        super(CommandType.START, bot);
        this.scrapperClient = scrapperClient;
    }

    @Override
    public void execute(Update update, boolean isInDialog) {
        if (!isInDialog) {
            Long chatId = update.message().chat().id();
            try {
                scrapperClient.registerChat(chatId);
                super.getBot().sendMessage(chatId, START_MESSAGE);
            } catch (WebClientResponseException e) {
                ScrapperException scrapperException = e.getResponseBodyAs(ScrapperException.class);
                super.getBot().sendMessage(chatId, scrapperException.getDescription());
            }
        }
    }
}
