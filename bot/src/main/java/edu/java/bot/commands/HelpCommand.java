package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;

public class HelpCommand extends Command {
    public static final String SLASH = "/";
    private final CommandList commandList;

    protected HelpCommand(String name, String description, Bot bot, CommandList commandList) {
        super(name, description, bot);
        this.commandList = commandList;
    }

    @Override
    public void execute(Update update) {
        StringBuilder stringBuilder = new StringBuilder();
        commandList.getCommandMap()
            .forEach((key, command) -> stringBuilder
                .append(SLASH)
                .append(command.getName())
                .append(" - ")
                .append(command.getDescription())
                .append("\n"));
        getBot().sendMessage(update.message().chat().id(), stringBuilder.toString());
    }
}
