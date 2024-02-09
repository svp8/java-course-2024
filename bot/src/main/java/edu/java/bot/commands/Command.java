package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;

public interface Command {
    void execute(Update update);
}
