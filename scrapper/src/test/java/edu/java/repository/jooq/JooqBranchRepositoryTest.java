package edu.java.repository.jooq;

import edu.java.entity.BranchEntity;
import edu.java.entity.LinkEntity;
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
class JooqBranchRepositoryTest extends IntegrationTest {
    @Autowired
    JooqBranchRepository jooqBranchRepository;
    @Autowired
    JooqLinkRepository jooqLinkRepository;
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", ()->"jooq");
    }
    @Test
    @Transactional
    @Rollback
    void getByNameAndLinkId() {
        LinkEntity linkEntity = jooqLinkRepository.add("333");
        BranchEntity expected = new BranchEntity("123", linkEntity.getId());
        jooqBranchRepository.add(expected);
        //when
        Optional<BranchEntity> actual = jooqBranchRepository.getByNameAndLinkId("123", expected.getLinkId());
        //then
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        LinkEntity linkEntity = jooqLinkRepository.add("333");
        jooqBranchRepository.add(new BranchEntity("123", linkEntity.getId()));
        jooqBranchRepository.add(new BranchEntity("124", linkEntity.getId()));
        //when
        List<BranchEntity> actual = jooqBranchRepository.getAllByLinkId(linkEntity.getId());
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
        LinkEntity linkEntity = jooqLinkRepository.add("333");
        BranchEntity branchEntity1 = jooqBranchRepository.add(new BranchEntity("123", linkEntity.getId()));
        BranchEntity branchEntity2 = jooqBranchRepository.add(new BranchEntity("124", linkEntity.getId()));
        //when
        jooqBranchRepository.delete(branchEntity1);
        //then
        Assertions.assertTrue(jooqBranchRepository.getByNameAndLinkId("123", linkEntity.getId()).isEmpty());
    }
}
