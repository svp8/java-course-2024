package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;

public class ListCommand implements Command {
    private final MessagingService messagingService;

    @Autowired
    public ListCommand(MessagingService messagingService) {
        this.messagingService = messagingService;
    }
    @Override
    public void execute(Update update) {

    }
}
