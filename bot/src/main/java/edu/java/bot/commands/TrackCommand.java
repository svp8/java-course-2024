package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;

public class TrackCommand extends Command {

    protected TrackCommand(String name, String description, Bot bot) {
        super(name, description, bot);
    }

    @Override
    public void execute(Update update) {

    }
}
