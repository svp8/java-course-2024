package edu.java.repository.jdbc;

import edu.java.entity.LinkEntity;
import edu.java.entity.PullEntity;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JdbcPullRepositoryTest extends IntegrationTest {
    @Autowired
    JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    JdbcPullRepository jdbcPullRepository;

    @Test
    @Transactional
    @Rollback
    void getById() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        PullEntity expected = new PullEntity(1, "123", linkEntity.getId());
        jdbcPullRepository.add(expected);
        //when
        Optional<PullEntity> actual = jdbcPullRepository.getById(1);
        //then
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        LinkEntity linkEntity = jdbcLinkRepository.add("333");
        jdbcPullRepository.add(new PullEntity(1, "123", linkEntity.getId()));
        jdbcPullRepository.add(new PullEntity(2, "124", linkEntity.getId()));
        //when
        List<PullEntity> actual = jdbcPullRepository.getAllByLinkId(linkEntity.getId());
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
        PullEntity branchEntity1 = jdbcPullRepository.add(new PullEntity(1, "123", linkEntity.getId()));
        PullEntity branchEntity2 = jdbcPullRepository.add(new PullEntity(2, "124", linkEntity.getId()));
        //when
        jdbcPullRepository.delete(branchEntity1);
        //then
        Assertions.assertTrue(jdbcPullRepository.getById(1).isEmpty());
    }
}
