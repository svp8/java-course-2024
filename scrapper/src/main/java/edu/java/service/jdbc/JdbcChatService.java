package edu.java.service.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.service.ChatService;
import java.util.List;
import org.springframework.http.HttpStatus;

public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    public JdbcChatService(JdbcChatRepository chatRepository, JdbcChatLinkRepository chatLinkRepository) {
        this.chatRepository = chatRepository;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Override
    public void registerChat(long chatId) {
        try {
            chatRepository.createChat(chatId);
        } catch (Exception e) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is already registered");
        }
    }

    @Override
    public void unregisterChat(long chatId) {
        chatRepository.deleteChat(chatId);
    }

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {
        return chatRepository.findChatsByLinkId(id);
    }
}
