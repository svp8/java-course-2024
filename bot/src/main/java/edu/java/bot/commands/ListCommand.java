package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import edu.java.bot.service.LinkService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends Command {
    public static final String NO_LINKS = "0 links are being tracked ";
    private final LinkService linkService;

    protected ListCommand(Bot bot, LinkService linkService) {
        super(CommandType.LIST, bot);
        this.linkService = linkService;
    }

    @Override
    public void execute(Update update) {
        List<Link> links = linkService.getAllTrackedLinksByUserId(update.message().from().id());
        Long chatId = update.message().chat().id();
        String linksString = links.toString();
        if (links.isEmpty()) {
            linksString = NO_LINKS;
        }
        super.getBot().sendMessage(chatId, linksString);
    }
}
