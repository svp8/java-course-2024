package edu.java.repository.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void createChat() {
        ChatEntity expected= new ChatEntity(1,null);
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
