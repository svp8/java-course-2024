package edu.java.service.jpa;

import edu.java.dto.Link;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.InvalidLinkFormatException;
import edu.java.exception.NoSuchLinkException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JpaLinkServiceTest extends IntegrationTest {
    public static final String URL = "https://stackoverflow.com/questions/30315448/java-jooq-insert-query-isnt-working";
    @Autowired
    JpaLinkService linkService;
    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaChatRepository chatRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    void track() {
        chatRepository.save(new ChatEntity(123, OffsetDateTime.now()));
        //when
        linkService.track("https://stackoverflow.com/questions/30315448/java-jooq-insert-query-isnt-working", 123);
        //then
        List<Link> allByChatId = linkService.getAllByChatId(123);
        Assertions.assertEquals(1, allByChatId.size());
    }

    @Test
    @DisplayName("Should throw if no chatId in db")
    @Transactional
    @Rollback
    void trackNoChatId() {
        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.track("test", 1));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should throw if duplicate link")
    void trackDuplicateLink() {
        chatRepository.save(new ChatEntity(100, OffsetDateTime.now()));
        linkService.track(URL, 100);
        Assertions.assertThrows(DuplicateLinkException.class, () -> linkService.track(URL, 100));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should throw if bad Uri")
    void trackBadUri() {
        chatRepository.save(new ChatEntity(100, OffsetDateTime.now()));
        Assertions.assertThrows(InvalidLinkFormatException.class, () -> linkService.track("test dsfds sdfdwsew", 100));
    }

    @Test
    @Transactional
    @Rollback
    void untrack() {
        chatRepository.save(new ChatEntity(123, OffsetDateTime.now()));
        String url = URL;
        linkService.track(url, 123);
        //when
        linkService.untrack(url, 123);
        //then
        List<Link> allByChatId = linkService.getAllByChatId(123);
        Assertions.assertTrue(allByChatId.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void untrackNoSuchLink() {
        chatRepository.save(new ChatEntity(123, OffsetDateTime.now()));
        linkService.track(URL, 123);

        Assertions.assertThrows(NoSuchLinkException.class, () -> linkService.untrack(URL + 1, 123));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should throw if no chatId in db")
    void untrackNoChatId() {
        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.untrack(URL, 1));
    }

    @Test
    @Transactional
    @Rollback
    void getAllByChatId() {
        chatRepository.save(new ChatEntity(123, OffsetDateTime.now()));
        linkService.track(URL, 123);
        linkService.track(URL + 2, 123);
        //when
        List<Link> allByChatId = linkService.getAllByChatId(123);
        //then
        Assertions.assertEquals(2, allByChatId.size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should throw if no chatId in db")
    void getAllByChatIdNoChatId() {
        Assertions.assertThrows(InvalidChatIdException.class, () -> linkService.getAllByChatId(1));
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        ChatEntity chat = chatRepository.save(new ChatEntity(123, OffsetDateTime.now()));
        List<ChatEntity> chats = new ArrayList<>();
        chats.add(chat);
        LinkEntity linkEntity = linkRepository.save(LinkEntity.builder()
            .name("name")
            .lastUpdatedAt(OffsetDateTime.now())
            .createdAt(OffsetDateTime.now())
            .chats(chats)
            .build());
        LinkEntity expected = new LinkEntity(linkEntity.getId(), "123", linkEntity.getCreatedAt(),
            linkEntity.getLastUpdatedAt().plusHours(2)
        );
        //when
        linkService.update(expected);
        //then
        Optional<LinkEntity> actual = linkRepository.findByName("123");
        Assertions.assertEquals(expected.getLastUpdatedAt(), actual.get().getLastUpdatedAt());
    }

    OffsetDateTime maxT = OffsetDateTime.of(2555, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    OffsetDateTime minT = OffsetDateTime.of(1, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    @Test
    @Transactional
    @Rollback
    void findAllLastUpdated() {
        LinkEntity entity = linkRepository.save(new LinkEntity("123", OffsetDateTime.now(), OffsetDateTime.now()));
        LinkEntity entity2 = linkRepository.save(new LinkEntity("12", OffsetDateTime.now(), OffsetDateTime.now()));
        LinkEntity entity3 = linkRepository.save(new LinkEntity("4", OffsetDateTime.now(), OffsetDateTime.now()));
        LinkEntity min = LinkEntity.builder()
            .id(entity.getId())
            .name(entity.getName())
            .lastUpdatedAt(minT)
            .createdAt(minT)
            .build();
        LinkEntity min2 = LinkEntity.builder()
            .id(entity2.getId())
            .name(entity2.getName())
            .lastUpdatedAt(minT)
            .createdAt(minT)
            .build();
        LinkEntity max = LinkEntity.builder()
            .id(entity3.getId())
            .name(entity3.getName())
            .lastUpdatedAt(maxT)
            .createdAt(maxT)
            .build();
        List<LinkEntity> expected = new ArrayList<>();
        expected.add(linkRepository.save(min));
        expected.add(linkRepository.save(min2));
        linkRepository.save(max);
//when
        List<LinkEntity> actual = linkRepository.findAllLastUpdated(0);
//then
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, actual);

    }
}
