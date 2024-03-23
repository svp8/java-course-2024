package edu.java.service.jooq;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.service.ChatService;
import java.util.List;
import org.springframework.http.HttpStatus;

public class JooqChatService implements ChatService {
    private final JooqChatRepository chatRepository;
    private final JooqChatLinkRepository chatLinkRepository;

    public JooqChatService(JooqChatRepository chatRepository, JooqChatLinkRepository chatLinkRepository) {
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
