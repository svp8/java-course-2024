package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand extends Command {

    protected TrackCommand(Bot bot) {
        super(CommandType.TRACK, bot);
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.message().chat().id();
        super.getBot().sendMessage(chatId, "tracking");
    }
}
