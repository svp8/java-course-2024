package edu.java.dto;

import java.util.List;

public record Update(Chat chat, Link link, List<LinkUpdate> linkUpdates) {
}
