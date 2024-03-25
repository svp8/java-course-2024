package edu.java.repository;

import edu.java.entity.ChatEntity;
import java.util.Optional;

public interface ChatRepository {
    ChatEntity createChat(long id);

    Optional<ChatEntity> getChatById(long id);

    void deleteChat(long id);
}
