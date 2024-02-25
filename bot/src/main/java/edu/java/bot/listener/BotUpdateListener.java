package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandList;
import edu.java.bot.model.Bot;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class BotUpdateListener implements UpdatesListener {
    private final Logger logger = LogManager.getLogger();
    private final CommandList commandList;
    private final HashMap<Long, Command> chatState = new HashMap<>();

    public BotUpdateListener(CommandList commandList, Bot bot) {
        this.commandList = commandList;
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Message message = update.message();
            logger.info(message.text());
            Long id = message.chat().id();
            if (message.text().startsWith("/")) {
                String commandIdentifier = message.text().split(" ")[0].toLowerCase();
                Command command = commandList.get(commandIdentifier);
                command.execute(update, false);
                chatState.put(id, command);
            } else if (chatState.containsKey(id)) {
                chatState.get(id).execute(update, true);
                chatState.remove(id);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
