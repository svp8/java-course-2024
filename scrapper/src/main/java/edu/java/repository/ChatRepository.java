package edu.java.repository;

import edu.java.entity.ChatEntity;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    ChatEntity createChat(long id);

    List<ChatEntity> findChatsByLinkId(int id);

    Optional<ChatEntity> getChatById(long id);

    void deleteChat(long id);
}
