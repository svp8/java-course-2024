package edu.java.service.jpa;

import edu.java.entity.BranchEntity;
import edu.java.entity.BranchId;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.repository.jpa.JpaBranchRepository;
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
class JpaBranchServiceTest extends IntegrationTest {
    @Autowired
    JpaBranchService jpaBranchService;
    @Autowired JpaLinkRepository linkRepository;
    @Autowired JpaChatRepository jpaChatRepository;
    @Autowired JpaBranchRepository jpaBranchRepository;

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
        List<BranchEntity> expected = new ArrayList<>();
        LinkEntity linkEntity = LinkEntity.builder()
            .name("name")
            .lastUpdatedAt(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .chats(List.of(chat))
            .build();
        LinkEntity saved = linkRepository.save(linkEntity);
        expected.add(jpaBranchService.add(new BranchEntity("1", linkEntity.getId())));
        expected.add(jpaBranchService.add(new BranchEntity("2", linkEntity.getId())));
        expected.add(jpaBranchService.add(new BranchEntity("3", linkEntity.getId())));
        //when
        List<BranchEntity> actual = jpaBranchService.getAllByLinkId(saved.getId());
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
        BranchEntity entity = new BranchEntity("1", linkEntity.getId());
        jpaBranchService.add(entity);
        //when
        jpaBranchService.delete(entity);
        //then
        Assertions.assertTrue(jpaBranchRepository.findById(new BranchId("1", linkEntity.getId())).isEmpty());
    }
}
