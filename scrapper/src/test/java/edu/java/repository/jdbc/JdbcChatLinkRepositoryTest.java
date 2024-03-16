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
        LinkEntity linkEntity = linkRepository.add("123");
        expected.add(linkEntity);
        LinkEntity linkEntity1 = linkRepository.add("211");
        expected.add(linkEntity1);
        chatLinkRepository.create(1, linkEntity.getId());
        chatLinkRepository.create(1, linkEntity1.getId());
        //when
        List<LinkEntity> actual = chatLinkRepository.findLinksByChatId(1);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    @Transactional
    void findChatsByLinkId() {
        List<ChatEntity> expected = new ArrayList<>();
        expected.add(chatRepository.createChat(1));
        expected.add(chatRepository.createChat(2));
        LinkEntity linkEntity = linkRepository.add("123");
        chatLinkRepository.create(1, linkEntity.getId());
        chatLinkRepository.create(2, linkEntity.getId());
        //when
        List<ChatEntity> actual = chatLinkRepository.findChatsByLinkId(linkEntity.getId());
        //then
        Assertions.assertEquals(expected,actual);

    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        chatRepository.createChat(1);
        List<LinkEntity> expected = new ArrayList<>();
        LinkEntity linkEntity = linkRepository.add("123");
        expected.add(linkEntity);
        LinkEntity linkEntity1 = linkRepository.add("211");
        expected.add(linkEntity1);
        chatLinkRepository.create(1, linkEntity.getId());
        chatLinkRepository.create(1, linkEntity1.getId());
        //when
        chatLinkRepository.remove(1, linkEntity.getId());
        //then
        List<LinkEntity> actual = chatLinkRepository.findLinksByChatId(1);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected.get(1), actual.get(0));
    }
}
