package edu.java.bot.model.request;

import edu.java.bot.model.scrapper.Update;
import java.util.List;

public record UpdateRequest(List<Update> updates) {
}
