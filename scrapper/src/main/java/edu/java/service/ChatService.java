package edu.java.service;

import edu.java.entity.ChatEntity;
import java.util.List;

public interface ChatService {
    void registerChat(long chatId);
    void unregisterChat(long chatId);

    List<ChatEntity> findChatsByLinkId(int id);
}
