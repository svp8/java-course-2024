package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.service.LinkService;
import java.util.List;

public class ListCommand extends Command {
    public static final String NO_LINKS = "0 links are being tracked ";
    private final LinkService linkService;

    protected ListCommand(String name, String description, Bot bot, LinkService linkService) {
        super(name, description, bot);
        this.linkService = linkService;
    }

    @Override
    public void execute(Update update) {
        List<Link> links = linkService.getAllTrackedLinksByUserId(update.message().from().id());
        if (links.isEmpty()) {
            super.getBot().sendMessage(update.message().chat().id(), NO_LINKS);
            return;
        }
        super.getBot().sendMessage(update.message().chat().id(), links.toString());
    }
}
