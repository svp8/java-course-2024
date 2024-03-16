package edu.java.repository.jdbc;

import edu.java.entity.AnswerEntity;
import edu.java.entity.CommentEntity;
import edu.java.entity.LinkEntity;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JdbcCommentRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    JdbcCommentRepository jdbcCommentRepository;
    OffsetDateTime offsetDateTime=OffsetDateTime.now();
    @Test
    @Transactional
    @Rollback
    void getById() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        CommentEntity expected = new CommentEntity(1, offsetDateTime, linkEntity.getId());
        jdbcCommentRepository.add(expected);
        //when
        Optional<CommentEntity> actual = jdbcCommentRepository.getById(1);
        //then
        Assertions.assertEquals(expected.getId(),actual.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        jdbcCommentRepository.add(new CommentEntity(1,offsetDateTime, linkEntity.getId()));
        jdbcCommentRepository.add(new CommentEntity(2,offsetDateTime, linkEntity.getId()));
        //when
        List<CommentEntity> actual = jdbcCommentRepository.getAllByLinkId(linkEntity.getId());
        //then
        Assertions.assertEquals(2,actual.size());

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
        CommentEntity commentEntity1 = jdbcCommentRepository.add(new CommentEntity(1,offsetDateTime, linkEntity.getId()));
        CommentEntity commentEntity2 = jdbcCommentRepository.add(new CommentEntity(2,offsetDateTime, linkEntity.getId()));
        //when
        jdbcCommentRepository.delete(commentEntity1);
        //then
        Assertions.assertTrue(jdbcCommentRepository.getById(1).isEmpty());
    }
}
