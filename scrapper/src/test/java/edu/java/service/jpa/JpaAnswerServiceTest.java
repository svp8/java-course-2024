package edu.java.service.jpa;

import edu.java.entity.AnswerEntity;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.repository.jpa.JpaAnswerRepository;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
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
class JpaAnswerServiceTest extends IntegrationTest {
    @Autowired
    JpaAnswerService jpaAnswerService;
    @Autowired JpaLinkRepository linkRepository;
    @Autowired JpaChatRepository jpaChatRepository;
    @Autowired JpaAnswerRepository jpaAnswerRepository;

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
        List<AnswerEntity> expected = new ArrayList<>();
        LinkEntity linkEntity = LinkEntity.builder()
            .name("name")
            .lastUpdatedAt(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .chats(List.of(chat))
            .build();
        LinkEntity saved = linkRepository.save(linkEntity);
        expected.add(jpaAnswerService.add(new AnswerEntity(1, OffsetDateTime.now(), saved.getId())));
        expected.add(jpaAnswerService.add(new AnswerEntity(2, OffsetDateTime.now(), saved.getId())));
        expected.add(jpaAnswerService.add(new AnswerEntity(3, OffsetDateTime.now(), saved.getId())));
        //when
        List<AnswerEntity> actual = jpaAnswerService.getAllByLinkId(saved.getId());
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
        AnswerEntity entity = new AnswerEntity(1, OffsetDateTime.now(), linkEntity.getId());
        jpaAnswerService.add(entity);
        //when
        jpaAnswerService.delete(entity);
        //then
        Assertions.assertTrue(jpaAnswerRepository.findById(1L).isEmpty());
    }
}
