package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandList;
import edu.java.bot.model.Bot;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class BotUpdateListener implements UpdatesListener {
    private final static Logger LOGGER = LogManager.getLogger();
    private final CommandList commandList;
    private final Map<Long, Command> chatState = new ConcurrentHashMap<>();
    private final Bot bot;

    public BotUpdateListener(CommandList commandList, Bot bot) {
        this.commandList = commandList;
        this.bot = bot;
        bot.setUpdatesListener(this);
    }

    /**
     * processes commands and text if in dialog
     *
     * @param list list with updates
     * @return result of update
     */
    @Override
    public int process(List<Update> list) {

        for (Update update : list) {
            Message message = update.message();
            LOGGER.info(message.text());
            Long id = message.chat().id();
            try {
                if (message.text().startsWith("/")) {
                    String commandIdentifier = message.text().split(" ")[0].toLowerCase();
                    Command command = commandList.get(commandIdentifier);
                    command.execute(update, false);
                    chatState.put(id, command);
                } else if (chatState.containsKey(id)) {
                    chatState.get(id).execute(update, true);
                    chatState.remove(id);
                }
            } catch (Exception e) {
                LOGGER.info(e);
                bot.sendMessage(id, e.getMessage());
            }

        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
