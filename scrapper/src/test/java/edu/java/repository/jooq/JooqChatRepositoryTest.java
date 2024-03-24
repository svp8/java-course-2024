package edu.java.repository.jooq;

import edu.java.entity.ChatEntity;
import edu.java.scrapper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatLinkRepository chatLinkRepository;
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqChatRepository chatRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    void createChat() {
        ChatEntity expected = new ChatEntity(1, null);
        //when
        ChatEntity actual = chatRepository.createChat(1);
        //then
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    @Transactional
    @Rollback
    void getChatById() {
        ChatEntity expected = chatRepository.createChat(1);
        //when
        ChatEntity actual = chatRepository.getChatById(1).get();
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChat() {
        chatRepository.createChat(1);
        chatRepository.deleteChat(1);
        //when
        Optional<ChatEntity> actual = chatRepository.getChatById(1);
        //then
        Assertions.assertTrue(actual.isEmpty());
    }
}
