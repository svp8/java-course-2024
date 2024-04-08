package edu.java.repository.jdbc;

import edu.java.entity.LinkEntity;
import edu.java.scrapper.IntegrationTest;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;
    OffsetDateTime maxT = OffsetDateTime.of(2555, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    OffsetDateTime minT = OffsetDateTime.of(1, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    @Test
    @Transactional
    @Rollback
    void addAndGetByLinkName() {
        LinkEntity actual = linkRepository.add("123");
        //then
        Assertions.assertEquals(linkRepository.getByLinkName("123").get(), actual);

    }

    @Test
    @Transactional
    @Rollback
    void update() {
        LinkEntity entity = linkRepository.add("123");
        LinkEntity actual =
            LinkEntity.builder().id(entity.getId()).createdAt(OffsetDateTime.MAX).lastUpdatedAt(OffsetDateTime.MIN)
                .name(entity.getName())
                .build();
        //when
        linkRepository.update(actual);
        //then
        Assertions.assertEquals(actual, linkRepository.getByLinkName("123").get());
    }

    @Test
    @Transactional
    @Rollback
    void findAllLastUpdated() {
        LinkEntity entity = linkRepository.add("123");
        LinkEntity entity2 = linkRepository.add("113");
        LinkEntity entity3 = linkRepository.add("133");
        LinkEntity min = LinkEntity.builder()
            .id(entity.getId())
            .name(entity.getName())
            .lastUpdatedAt(minT)
            .createdAt(OffsetDateTime.MAX)
            .build();
        LinkEntity min2 = LinkEntity.builder()
            .id(entity2.getId())
            .name(entity2.getName())
            .lastUpdatedAt(minT)
            .createdAt(OffsetDateTime.MAX)
            .build();
        LinkEntity max = LinkEntity.builder()
            .id(entity3.getId())
            .name(entity3.getName())
            .lastUpdatedAt(maxT)
            .createdAt(OffsetDateTime.MAX)
            .build();
        List<LinkEntity> expected = new ArrayList<>();
        expected.add(linkRepository.update(min));
        expected.add(linkRepository.update(min2));
        linkRepository.update(max);
//when
        List<LinkEntity> actual = linkRepository.findAllLastUpdated(Duration.ZERO);
//then
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, actual);

    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        LinkEntity entity = linkRepository.add("123");
        LinkEntity entity2 = linkRepository.add("113");
        //when
        linkRepository.remove(entity.getId());
        //then
        Assertions.assertTrue(linkRepository.getByLinkName("123").isEmpty());
    }
}
