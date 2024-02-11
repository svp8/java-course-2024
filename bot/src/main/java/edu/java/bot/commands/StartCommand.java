package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;

public class StartCommand extends Command {
    private static final String START_MESSAGE = "New user registered";

    protected StartCommand(String name, String description, Bot bot) {
        super(name, description, bot);
    }

    @Override
    public void execute(Update update) {
        Message message = update.message();
        super.getBot().sendMessage(message.chat().id(), START_MESSAGE);
    }
}
