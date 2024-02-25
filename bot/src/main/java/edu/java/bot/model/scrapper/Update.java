package edu.java.bot.model.scrapper;

import java.util.List;

public record Update(Chat chat, List<LinkUpdate> linkUpdates) {
}
