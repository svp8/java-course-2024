package edu.java.service.jooq;

import edu.java.dto.Link;
import edu.java.entity.LinkEntity;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.InvalidLinkFormatException;
import edu.java.exception.NoSuchLinkException;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.LinkService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JooqLinkServiceTest extends IntegrationTest {
    public static final String URL = "https://stackoverflow.com/questions/30315448/java-jooq-insert-query-isnt-working";
    @Autowired
    LinkService linkService;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    void track() {
        chatRepository.createChat(123);
        //when
        linkService.track("https://stackoverflow.com/questions/30315448/java-jooq-insert-query-isnt-working", 123);
        //then
        List<LinkEntity> allByChatId = linkRepository.findAllByChatId(123);
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
        chatRepository.createChat(100);
        linkService.track(URL, 100);
        Assertions.assertThrows(DuplicateLinkException.class, () -> linkService.track(URL, 100));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should throw if bad Uri")
    void trackBadUri() {
        chatRepository.createChat(100);
        Assertions.assertThrows(InvalidLinkFormatException.class, () -> linkService.track("test dsfds sdfdwsew", 100));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should throw if chat already registered")
    void registerChatIdDuplicate() {
        chatRepository.createChat(100);
        Assertions.assertThrows(DuplicateKeyException.class, () -> chatRepository.createChat(100));
    }

    @Test
    @Transactional
    @Rollback
    void untrack() {
        chatRepository.createChat(123);
        String url = URL;
        linkService.track(url, 123);
        //when
        linkService.untrack(url, 123);
        //then
        List<LinkEntity> allByChatId = linkRepository.findAllByChatId(123);
        Assertions.assertTrue(allByChatId.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void untrackNoSuchLink() {
        chatRepository.createChat(123);
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
        chatRepository.createChat(123);
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
        LinkEntity linkEntity = linkRepository.add("123");
        LinkEntity expected = new LinkEntity(linkEntity.getId(), "123", linkEntity.getCreatedAt(),
            linkEntity.getLastUpdatedAt().plusHours(2)
        );
        //when
        linkService.update(expected);
        //then
        Optional<LinkEntity> actual = linkRepository.getByLinkName("123");
        Assertions.assertEquals(expected, actual.get());
    }
}
