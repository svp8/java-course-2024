package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.entity.CommentEntity;
import edu.java.entity.LinkEntity;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaCommentRepository;
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
class JpaCommentServiceTest extends IntegrationTest {
    @Autowired
    JpaCommentService jpaCommentService;
    @Autowired JpaLinkRepository linkRepository;
    @Autowired JpaChatRepository jpaChatRepository;
    @Autowired JpaCommentRepository jpaCommentRepository;
    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", ()->"jpa");
    }
    @Test
    @Transactional
    @Rollback
    void getAllByLinkId() {
        ChatEntity chat = new ChatEntity(123, OffsetDateTime.now());
        jpaChatRepository.save(chat);
        List<CommentEntity> expected = new ArrayList<>();
        LinkEntity linkEntity = LinkEntity.builder()
            .name("name")
            .lastUpdatedAt(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .chats(List.of(chat))
            .build();
        LinkEntity saved = linkRepository.save(linkEntity);
        expected.add(jpaCommentService.add(new CommentEntity(1,OffsetDateTime.now(),linkEntity.getId())));
        expected.add(jpaCommentService.add(new CommentEntity(1,OffsetDateTime.now(),linkEntity.getId())));
        expected.add(jpaCommentService.add(new CommentEntity(1,OffsetDateTime.now(),linkEntity.getId())));

        //when
        List<CommentEntity> actual = jpaCommentService.getAllByLinkId(saved.getId());
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
        CommentEntity entity = new CommentEntity(1,OffsetDateTime.now(),linkEntity.getId());
        jpaCommentService.add(entity);
        //when
        jpaCommentService.delete(entity);
        //then
        Assertions.assertTrue(jpaCommentRepository.findById(entity.getId()).isEmpty());
    }
}
