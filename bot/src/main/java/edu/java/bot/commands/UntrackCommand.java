package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UntrackCommand extends Command {
    private final ScrapperClient scrapperClient;
    private final Map<Long, List<Link>> linksByChat;

    public UntrackCommand(Bot bot, ScrapperClient scrapperClient) {
        super(CommandType.UNTRACK, bot);
        this.scrapperClient = scrapperClient;
        this.linksByChat = new HashMap<>();
    }

    @Override
    public void execute(Update update, boolean isInDialog) {
        Long chatId = update.message().chat().id();
        if (!isInDialog) {
            List<Link> links = scrapperClient.getLinkList(chatId).getLinks();
            linksByChat.put(chatId, links);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < links.size(); i++) {
                stringBuilder.append((i + 1))
                    .append(". ")
                    .append(links.get(i)
                        .getUri()
                        .toString())
                    .append("\n");
            }
            stringBuilder.append("Choose number of link to untrack");
            super.getBot().sendMessage(chatId, stringBuilder.toString());
        } else {
            if (isNumeric(update.message().text())) {
                int number = Integer.parseInt(update.message().text());
                List<Link> links = linksByChat.get(chatId);
                if (number > 0 && number <= links.size()) {
                    scrapperClient.untrackLink(chatId, links.get(number - 1).getUri().toString());
                    super.getBot().sendMessage(chatId, update.message().text() + " untracked");
                } else {
                    super.getBot().sendMessage(chatId, "No such link number");
                }
            } else {
                super.getBot().sendMessage(chatId, "Wrong input format (send number)");
            }
            linksByChat.remove(chatId);
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
