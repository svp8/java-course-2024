package edu.java.repository;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import java.util.List;

public interface ChatLinkRepository {
    List<LinkEntity> findLinksByChatId(int id);
    List<ChatEntity> findChatsByLinkId(long id);
}
