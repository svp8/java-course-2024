package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import edu.java.bot.service.LinkService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends Command {
    public static final String NO_LINKS = "0 links are being tracked ";
    private final ScrapperClient scrapperClient;

    protected ListCommand(Bot bot, ScrapperClient scrapperClient) {
        super(CommandType.LIST, bot);
        this.scrapperClient = scrapperClient;
    }

    @Override
    public void execute(Update update, boolean isInDialog) {
        if (!isInDialog) {
            Long chatId = update.message().chat().id();
            List<Link> links = scrapperClient.getLinkList(chatId).getLinks();
            String linksString = links.toString();
            if (links.isEmpty()) {
                linksString = NO_LINKS;
            }
            super.getBot().sendMessage(chatId, linksString);
        }
    }
}
