package edu.java.repository.jdbc;

import edu.java.entity.QuestionEntity;
import edu.java.entity.RepositoryEntity;
import edu.java.repository.stack.StackRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JdbcStackRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcStackRepository stackRepository;
    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;


    @Test
    @Transactional
    @Rollback
    void update() {
        QuestionEntity entity=new QuestionEntity(1,1,1);
      stackRepository.add(entity);
        QuestionEntity expected=new QuestionEntity(1,2,1);
        //when
        stackRepository.update(expected);
        //then
        Assertions.assertEquals(expected,stackRepository.getQuestion(1).get());
    }

    @Test
    @Transactional
    @Rollback
    void addAndGet() {
        QuestionEntity entity=new QuestionEntity(1,1,1);
        //when
        stackRepository.add(entity);
        //then
        Assertions.assertEquals(entity,stackRepository.getQuestion(1).get());
    }
}
