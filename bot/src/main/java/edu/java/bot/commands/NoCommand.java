package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import org.springframework.stereotype.Component;

@Component
public class NoCommand extends Command {
    private final String answer = "There is no such command";

    protected NoCommand(Bot bot) {
        super(CommandType.NO, bot);
    }

    @Override
    public void execute(Update update, boolean isInDialog) {
        Message message = update.message();
        super.getBot().sendMessage(message.chat().id(), answer);
    }
}
