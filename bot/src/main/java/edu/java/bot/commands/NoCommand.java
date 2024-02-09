package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;

public class NoCommand implements Command {
    private final String answer="There is no such command";
    private final MessagingService messagingService;

    @Autowired
    public NoCommand(MessagingService messagingService) {
        this.messagingService = messagingService;
    }
    @Override
    public void execute(Update update) {
        Message message=update.message();
        messagingService.sendMessage(message.chat().id(),answer);
    }
}
