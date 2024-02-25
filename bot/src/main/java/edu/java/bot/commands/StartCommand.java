package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import org.springframework.stereotype.Component;

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
            scrapperClient.registerChat(chatId);
            super.getBot().sendMessage(chatId, START_MESSAGE);
        }
    }
}
