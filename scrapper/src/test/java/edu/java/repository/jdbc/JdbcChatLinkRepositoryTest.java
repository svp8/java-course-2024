package edu.java.repository.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class JdbcChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    @BeforeEach
    void init() {
    }

    @Test
    @Transactional
    @Rollback
    void createAndFindLinksByChatId() {
        chatRepository.createChat(1);
        List<LinkEntity> expected = new ArrayList<>();
        expected.add(linkRepository.add("123"));
        expected.add(linkRepository.add("211"));
        chatLinkRepository.create(1, 1);
        chatLinkRepository.create(1, 2);
        //when
        List<LinkEntity> actual = chatLinkRepository.findLinksByChatId(1);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Rollback
    void findChatsByLinkId() {
        List<ChatEntity> expected = new ArrayList<>();
        expected.add(chatRepository.createChat(1));
        expected.add(chatRepository.createChat(2));
        linkRepository.add("123");
        chatLinkRepository.create(1, 1);
        chatLinkRepository.create(2, 1);
        //when
        List<ChatEntity> actual = chatLinkRepository.findChatsByLinkId(1);
        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        chatRepository.createChat(1);
        List<LinkEntity> expected = new ArrayList<>();
        expected.add(linkRepository.add("123"));
        expected.add(linkRepository.add("211"));
        chatLinkRepository.create(1, 1);
        chatLinkRepository.create(1, 2);
        //when
        chatLinkRepository.remove(1, 1);
        //then
        List<LinkEntity> actual = chatLinkRepository.findLinksByChatId(1);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected.get(1), actual.get(0));
    }
}
