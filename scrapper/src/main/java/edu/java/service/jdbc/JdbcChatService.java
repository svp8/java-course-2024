package edu.java.service.jdbc;

import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
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
}
