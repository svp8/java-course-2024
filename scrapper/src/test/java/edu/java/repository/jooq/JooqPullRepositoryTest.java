package edu.java.repository.jooq;

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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JooqPullRepositoryTest extends IntegrationTest {
    @Autowired
    JooqLinkRepository linkRepository;
    @Autowired
    JooqPullRepository pullRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    void getById() {
        LinkEntity linkEntity = linkRepository.add("333");
        PullEntity expected = new PullEntity(1, "123", linkEntity.getId());
        pullRepository.add(expected);
        //when
        Optional<PullEntity> actual = pullRepository.getById(1);
        //then
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        LinkEntity linkEntity = linkRepository.add("333");
        pullRepository.add(new PullEntity(1, "123", linkEntity.getId()));
        pullRepository.add(new PullEntity(2, "124", linkEntity.getId()));
        //when
        List<PullEntity> actual = pullRepository.getAllByLinkId(linkEntity.getId());
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
        LinkEntity linkEntity = linkRepository.add("333");
        PullEntity branchEntity1 = pullRepository.add(new PullEntity(1, "123", linkEntity.getId()));
        PullEntity branchEntity2 = pullRepository.add(new PullEntity(2, "124", linkEntity.getId()));
        //when
        pullRepository.delete(branchEntity1);
        //then
        Assertions.assertTrue(pullRepository.getById(1).isEmpty());
    }
}
