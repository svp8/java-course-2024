package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.CommandList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotUpdateListener implements UpdatesListener {
    private final Logger logger = LogManager.getLogger();
    private final CommandList commandList;

    public BotUpdateListener(CommandList commandList) {
        this.commandList = commandList;
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Message message = update.message();
            logger.info(message.text());
            if (message.text().startsWith("/")) {
                String commandIdentifier = message.text().split(" ")[0].toLowerCase();
                commandList.get(commandIdentifier).execute(update);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
