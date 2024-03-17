package edu.java.repository;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import java.util.List;

public interface ChatLinkRepository {
    List<LinkEntity> findLinksByChatId(long id);

    List<ChatEntity> findChatsByLinkId(int id);

    void create(long chatId, int linkId);

    void remove(long chatId, int linkId);
}
