package edu.java.dto;

import java.util.List;

public record Update(long chatId, List<LinkUpdate> linkUpdates) {
}
