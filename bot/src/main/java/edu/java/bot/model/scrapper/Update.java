package edu.java.bot.model.scrapper;

import edu.java.bot.model.Link;
import java.util.List;

public record Update(Chat chat, Link link, List<LinkUpdate> linkUpdates) {
}
