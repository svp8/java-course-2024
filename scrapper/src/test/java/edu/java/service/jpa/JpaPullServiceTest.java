package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.entity.PullEntity;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaPullRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JpaPullServiceTest extends IntegrationTest {
    @Autowired
    JpaPullService jpaPullService;
    @Autowired JpaLinkRepository linkRepository;
    @Autowired JpaChatRepository jpaChatRepository;
    @Autowired JpaPullRepository jpaPullRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        ChatEntity chat = new ChatEntity(123, OffsetDateTime.now());
        jpaChatRepository.save(chat);
        List<PullEntity> expected = new ArrayList<>();
        LinkEntity linkEntity = LinkEntity.builder()
            .name("name")
            .lastUpdatedAt(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .chats(List.of(chat))
            .build();
        LinkEntity saved = linkRepository.save(linkEntity);
        expected.add(jpaPullService.add(new PullEntity(1, "123", saved.getId())));
        expected.add(jpaPullService.add(new PullEntity(2, "123", saved.getId())));
        expected.add(jpaPullService.add(new PullEntity(3, "123", saved.getId())));

        //when
        List<PullEntity> actual = jpaPullService.getAllByLinkId(saved.getId());
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    @Transactional
    void delete() {
        ChatEntity chat = new ChatEntity(123, OffsetDateTime.now());
        jpaChatRepository.save(chat);
        LinkEntity linkEntity = LinkEntity.builder()
            .name("name")
            .lastUpdatedAt(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .chats(List.of(chat))
            .build();
        linkRepository.save(linkEntity);
        PullEntity entity = new PullEntity(1, "123", linkEntity.getId());
        jpaPullService.add(entity);
        //when
        jpaPullService.delete(entity);
        //then
        Assertions.assertTrue(jpaPullRepository.findById(entity.getId()).isEmpty());
    }
}
