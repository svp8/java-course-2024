package edu.java.bot.model.scrapper;

import java.util.List;

public record Update(long chatId, List<LinkUpdate> linkUpdates) {
}
