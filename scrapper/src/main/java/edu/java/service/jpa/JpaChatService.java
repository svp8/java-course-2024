package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jpa.JpaChatRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import edu.java.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    public JpaChatService(JpaChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void registerChat(long chatId) {
        if (chatRepository.findById(chatId).isPresent()) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is already registered");
        }
        chatRepository.save(new ChatEntity(chatId, OffsetDateTime.now()));
    }

    @Override
    public void unregisterChat(long chatId) {
        Optional<ChatEntity> chatRepositoryById = chatRepository.findById(chatId);
        if (chatRepositoryById.isEmpty()) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is already registered");
        }
        chatRepository.delete(chatRepositoryById.get());
    }
}
