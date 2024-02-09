package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;

public class StartCommand implements Command {
    private final MessagingService messagingService;
    String START_MESSAGE="New user registered";

    @Autowired
    public StartCommand(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public void execute(Update update) {
        Message message=update.message();
        messagingService.sendMessage(message.chat().id(),START_MESSAGE);
    }
}
