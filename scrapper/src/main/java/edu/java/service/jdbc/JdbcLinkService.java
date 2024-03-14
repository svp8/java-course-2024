package edu.java.service.jdbc;

import edu.java.dto.Link;
import edu.java.entity.LinkEntity;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.InvalidLinkFormatException;
import edu.java.exception.LinkNotTrackedException;
import edu.java.exception.NoSuchLinkException;
import edu.java.exception.URIException;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.service.LinkService;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {
    public static final String CHAT_ISN_T_REGISTERED = "Chat isn`t registered";
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository jdbcChatRepository;
    private final JdbcChatLinkRepository jdbcChatLinkRepository;

    public JdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcChatRepository jdbcChatRepository,
        JdbcChatLinkRepository jdbcChatLinkRepository
    ) {
        this.linkRepository = linkRepository;
        this.jdbcChatRepository = jdbcChatRepository;
        this.jdbcChatLinkRepository = jdbcChatLinkRepository;
    }

    @Override
    @Transactional
    public Link track(String name, long chatId) {
        checkChatIdInDb(chatId);
        if (name.startsWith("https://github.com")) {
            LinkUtils.parseGithubLink(name);
        } else if (name.startsWith("https://stackoverflow.com/questions/")) {
            LinkUtils.parseStackLink(name);
        } else {
            throw new InvalidLinkFormatException(HttpStatus.BAD_REQUEST.value(), "Wrong link format");
        }

        Optional<LinkEntity> link = linkRepository.getByLinkName(name);
        if (link.isPresent()) {
            throw new DuplicateLinkException(HttpStatus.BAD_REQUEST.value(), "Link is already tracked");
        } else {
            List<LinkEntity> linkList = linkRepository.findAllByChatId(chatId);
            if (linkList.stream().anyMatch(l -> l.getName().equals(name))) {
                throw new DuplicateLinkException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Link is already tracked by this chat"
                );
            }
            try {
                Link createdLink = new Link(new URI(name.trim()));
                LinkEntity linkEntity = linkRepository.add(name);
                jdbcChatLinkRepository.create(chatId, linkEntity.getId());
                return createdLink;
            } catch (URISyntaxException e) {
                throw new URIException(HttpStatus.BAD_REQUEST.value(), "Bad Uri");
            }
        }
    }

    @Override
    @Transactional
    public void untrack(String name, long chatId) {
        checkChatIdInDb(chatId);
        Optional<LinkEntity> link = linkRepository.getByLinkName(name);
        if (link.isEmpty()) {
            throw new NoSuchLinkException(HttpStatus.NOT_FOUND.value(), "Link is not created");
        } else {
            List<LinkEntity> linkList = linkRepository.findAllByChatId(chatId);
            //если к чату не привязана ссылка
            if (linkList == null || linkList.stream().noneMatch(l -> l.getName().equals(name))) {
                throw new LinkNotTrackedException(HttpStatus.NOT_FOUND.value(), "Link is not tracked by this chat");
            }
            jdbcChatLinkRepository.remove(chatId, link.get().getId());
        }
    }

    private void checkChatIdInDb(long chatId) {
        //if no such chat in db, throw exception
        if (jdbcChatRepository.getChatById(chatId).isEmpty()) {
            throw new InvalidChatIdException(HttpStatus.NOT_FOUND.value(), CHAT_ISN_T_REGISTERED);
        }
    }

    @Override
    public List<Link> getAllByChatId(long chatId) {
        checkChatIdInDb(chatId);
        List<LinkEntity> allByChatId = linkRepository.findAllByChatId(chatId);
        if (allByChatId == null) {
            return null;
        }
        return allByChatId.stream().map(linkEntity -> {
            try {
                return new Link(new URI(linkEntity.getName()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}