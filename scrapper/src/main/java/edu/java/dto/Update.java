package edu.java.dto;

import java.util.List;

public record Update(Chat chat, List<LinkUpdate> linkUpdates) {
}
