package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends Command {

    protected HelpCommand(Bot bot) {
        super(CommandType.HELP, bot);
    }

    @Override
    public void execute(Update update) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(CommandType.values())
            .filter(commandType -> !commandType.equals(CommandType.NO))
            .forEach((command) -> stringBuilder
                .append(command.getName())
                .append(" - ")
                .append(command.getDescription())
                .append("\n"));
        getBot().sendMessage(update.message().chat().id(), stringBuilder.toString());
    }
}
