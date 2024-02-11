package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;

public class NoCommand extends Command {
    private final String answer = "There is no such command";

    protected NoCommand(String name, String description, Bot bot) {
        super(name, description, bot);
    }

    @Override
    public void execute(Update update) {
        Message message = update.message();
        super.getBot().sendMessage(message.chat().id(), answer);
    }
}
