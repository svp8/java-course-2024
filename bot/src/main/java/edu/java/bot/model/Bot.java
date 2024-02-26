package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot extends TelegramBot implements AutoCloseable {
    @Autowired
    public Bot(ApplicationConfig applicationConfig) {
        super(applicationConfig.telegramToken());
        registerCommands();
    }

    private void registerCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        Arrays.stream(CommandType.values()).forEach(commandType -> {
            botCommands.add(new BotCommand(commandType.getName(), commandType.getDescription()));
        });
        SetMyCommands setMyCommands = new SetMyCommands(botCommands.toArray(BotCommand[]::new));
        this.execute(setMyCommands);
    }

    public SendResponse sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        return execute(sendMessage);
    }

    @Override
    public void close() {
        super.shutdown();
    }
}
