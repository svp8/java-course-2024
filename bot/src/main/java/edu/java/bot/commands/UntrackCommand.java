package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class UntrackCommand extends Command {
    public static final String NO_SUCH_LINK_NUMBER = "No such link number";
    public static final String WRONG_INPUT_FORMAT = "Wrong input format (send number)";
    private final ScrapperClient scrapperClient;
    private final Map<Long, List<Link>> linksByChat;

    public UntrackCommand(Bot bot, ScrapperClient scrapperClient) {
        super(CommandType.UNTRACK, bot);
        this.scrapperClient = scrapperClient;
        this.linksByChat = new HashMap<>();
    }

    /**
     * Gets list of links of specific chat and sends it to chat
     *
     * @param chatId chatid
     */
    private void sendList(Long chatId) {
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
    }

    @Override
    public void execute(Update update, boolean isInDialog) {
        Long chatId = update.message().chat().id();
        if (!isInDialog) {
            try {
                sendList(chatId);
            } catch (WebClientResponseException e) {
                ScrapperException scrapperException = e.getResponseBodyAs(ScrapperException.class);
                super.getBot().sendMessage(chatId, scrapperException.getDescription());
            }
        } else {
            if (isNumeric(update.message().text())) {
                int number = Integer.parseInt(update.message().text());
                List<Link> links = linksByChat.get(chatId);
                if (number > 0 && number <= links.size()) {
                    try {
                        scrapperClient.untrackLink(chatId, links.get(number - 1).getUri().toString());
                        super.getBot().sendMessage(chatId, update.message().text() + " untracked");
                    } catch (WebClientResponseException e) {
                        ScrapperException scrapperException = e.getResponseBodyAs(ScrapperException.class);
                        super.getBot().sendMessage(chatId, scrapperException.getDescription());
                    }
                } else {
                    super.getBot().sendMessage(chatId, NO_SUCH_LINK_NUMBER);
                }
            } else {
                super.getBot().sendMessage(chatId, WRONG_INPUT_FORMAT);
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
