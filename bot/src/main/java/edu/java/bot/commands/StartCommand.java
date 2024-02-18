package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends Command {
    public static final String START_MESSAGE = "New user registered";

    protected StartCommand(Bot bot) {
        super(CommandType.START, bot);
    }

    @Override
    public void execute(Update update) {
        Message message = update.message();
        super.getBot().sendMessage(message.chat().id(), START_MESSAGE);
    }
}
