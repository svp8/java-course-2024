package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.service.ChatService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

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

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {
        return chatRepository.findByLinks_Id(id);
    }
}
