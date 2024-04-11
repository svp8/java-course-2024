package edu.java.service.jooq;

import edu.java.entity.ChatEntity;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.service.ChatService;
import java.util.List;

public class JooqChatService implements ChatService {
    private final JooqChatRepository chatRepository;

    public JooqChatService(JooqChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void registerChat(long chatId) {
        chatRepository.createChat(chatId);
    }

    @Override
    public void unregisterChat(long chatId) {
//        if (chatRepository.getChatById(chatId).isEmpty()) {
//            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is not registered");
//        }
        chatRepository.deleteChat(chatId);
    }

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {
        return chatRepository.findChatsByLinkId(id);
    }
}
