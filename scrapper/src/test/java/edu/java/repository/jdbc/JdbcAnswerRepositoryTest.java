package edu.java.repository.jdbc;

import edu.java.entity.AnswerEntity;
import edu.java.entity.LinkEntity;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
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
class JdbcAnswerRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    JdbcAnswerRepository jdbcAnswerRepository;

    OffsetDateTime offsetDateTime = OffsetDateTime.now();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    void getById() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        AnswerEntity expected = new AnswerEntity(1, offsetDateTime, linkEntity.getId());
        jdbcAnswerRepository.add(expected);
        //when
        Optional<AnswerEntity> actual = jdbcAnswerRepository.getById(expected.getId());
        //then
        Assertions.assertEquals(expected.getId(), actual.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        jdbcAnswerRepository.add(new AnswerEntity(1, offsetDateTime, linkEntity.getId()));
        jdbcAnswerRepository.add(new AnswerEntity(2, offsetDateTime, linkEntity.getId()));
        //when
        List<AnswerEntity> actual = jdbcAnswerRepository.getAllByLinkId(linkEntity.getId());
        //then
        Assertions.assertEquals(2, actual.size());

    }

    @Test
    @Transactional
    @Rollback
    void update() {
    }

    @Test
    @Transactional
    @Rollback
    void delete() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        AnswerEntity answerEntity1 = jdbcAnswerRepository.add(new AnswerEntity(1, offsetDateTime, linkEntity.getId()));
        AnswerEntity answerEntity2 = jdbcAnswerRepository.add(new AnswerEntity(2, offsetDateTime, linkEntity.getId()));
        //when
        jdbcAnswerRepository.delete(answerEntity1);
        //then
        Assertions.assertTrue(jdbcAnswerRepository.getById(answerEntity1.getId()).isEmpty());
    }
}
