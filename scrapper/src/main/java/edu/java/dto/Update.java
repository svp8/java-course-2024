package edu.java.dto;

import edu.java.entity.ChatEntity;
import java.util.List;

public record Update(Chat chat,Link link, List<LinkUpdate> linkUpdates) {
}
