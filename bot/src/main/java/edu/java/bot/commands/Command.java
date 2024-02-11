package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import lombok.Getter;

@Getter
public abstract class Command {
    private final String name;
    private final String description;
    private final Bot bot;

    protected Command(String name, String description, Bot bot) {
        this.name = name;
        this.description = description;
        this.bot = bot;
    }

    abstract public void execute(Update update);
}
