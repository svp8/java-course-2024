package edu.java.bot.commands;

import edu.java.bot.model.Bot;
import edu.java.bot.service.LinkService;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class CommandList {
    private final Map<String, Command> commandMap;
    private final NoCommand noCommand;
    private final LinkService linkService;

    public CommandList(Bot bot, LinkService linkService) {
        this.commandMap = Collections.unmodifiableMap(Map.of(
            "/start", new StartCommand("start", "register new user", bot),
            "/help", new HelpCommand("help", "show all commands", bot, this),
            "/track", new TrackCommand("track", "track link", bot),
            "/untrack", new UntrackCommand("untrack", "untrack link", bot),
            "/list", new ListCommand("list", "links list", bot, linkService)
        ));
        this.noCommand = new NoCommand("error", "shows on error", bot);
        this.linkService = linkService;
    }

    public Command get(String name) {
        return commandMap.getOrDefault(name, noCommand);
    }
}
