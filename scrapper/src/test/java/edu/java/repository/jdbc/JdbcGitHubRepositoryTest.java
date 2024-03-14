package edu.java.repository.jdbc;

import edu.java.entity.RepositoryEntity;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JdbcGitHubRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcGitHubRepository jdbcGitHubRepository;
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
        RepositoryEntity entity = new RepositoryEntity(1, 1, 1);
        jdbcGitHubRepository.add(entity);
        RepositoryEntity expected = new RepositoryEntity(1, 2, 1);
        //when
        jdbcGitHubRepository.update(expected);
        //then
        Assertions.assertEquals(expected,jdbcGitHubRepository.getRepo(1).get());
    }

    @Test
    @Transactional
    @Rollback
    void addAndGet() {
        RepositoryEntity expected = new RepositoryEntity(1, 1, 1);
       //when
        jdbcGitHubRepository.add(expected);
       //then
        Assertions.assertEquals(expected,jdbcGitHubRepository.getRepo(1).get());
    }
}
