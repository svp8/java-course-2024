package edu.java.service.jooq;

import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.service.ChatService;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JooqChatService implements ChatService {
    private final JooqChatRepository chatRepository;

    public JooqChatService(JooqChatRepository chatRepository) {
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
