package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand extends Command {

    protected UntrackCommand(Bot bot) {
        super(CommandType.UNTRACK, bot);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.message().chat().id();
        super.getBot().sendMessage(chatId, "unTracking");
    }
}
