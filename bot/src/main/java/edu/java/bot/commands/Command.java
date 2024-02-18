package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import lombok.Getter;

@Getter
public abstract class Command {
    private final Bot bot;
    private final CommandType type;

    protected Command(CommandType type, Bot bot) {
        this.bot = bot;
        this.type = type;
    }

    public abstract void execute(Update update);
}
