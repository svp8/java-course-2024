package edu.java.service.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.service.ChatService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    public JdbcChatService(JdbcChatRepository chatRepository, JdbcChatLinkRepository chatLinkRepository) {
        this.chatRepository = chatRepository;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Override
    public void registerChat(long chatId) {
        if (chatRepository.getChatById(chatId).isPresent()) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is already registered");
        }
        chatRepository.createChat(chatId);
    }

    @Override
    public void unregisterChat(long chatId) {
        if (chatRepository.getChatById(chatId).isEmpty()) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is not registered");
        }
        chatRepository.deleteChat(chatId);
    }

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {
        return chatLinkRepository.findChatsByLinkId(id);
    }
}
