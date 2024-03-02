package edu.java.repository.jdbc;

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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init(){
    }

    @Test
    @Transactional
    @Rollback
    void findLinksByChatId() {
        chatRepository.createChat(1);
        List<LinkEntity> expected=new ArrayList<>();
        expected.add(linkRepository.add("123"));
        expected.add(linkRepository.add("211"));
        chatLinkRepository.create(1,1);
        chatLinkRepository.create(1,2);
        //when
        List<LinkEntity> actual = chatLinkRepository.findLinksByChatId(1);
        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void findChatsByLinkId() {
    }

    @Test
    void create() {
    }

    @Test
    void remove() {
    }
}
