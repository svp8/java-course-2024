package edu.java.service;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.NoSuchLinkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class LinkServiceImpl implements LinkService {
    public static final Logger LOGGER= LogManager.getLogger();
    public Optional<Link> getByNameAndChatId(String name, long chatId) {
        return Optional.empty();
    }

    public Link track(String name, long chatId) {
        LOGGER.info(name);
        Optional<Link> link = getByNameAndChatId(name, chatId);
        if (link.isPresent()) {
            throw new DuplicateLinkException(HttpStatus.BAD_REQUEST.value(), "Link is already tracked");
        } else {
            try {
                return new Link(new URI(name.trim()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registerChatId(long chatId) {
        Optional<Chat> chat = Optional.of(new Chat());
        //check if chat not in db
        if (chat.isEmpty()) {
            return;
        } else {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat already registered");
        }
    }

    public void untrack(String name, long chatId) {
        Optional<Link> link = getByNameAndChatId(name.trim(), chatId);
        if (link.isPresent()) {
            //delete in db
            return;
        } else {
            throw new NoSuchLinkException(HttpStatus.NOT_FOUND.value(), "No such link");
        }
    }

    public List<Link> getAllByChatId(long chatId) {
        //if no such chat in db, throw exception
        if (chatId == 0) {
            throw new InvalidChatIdException(HttpStatus.NOT_FOUND.value(), "Chat isn`t registered");
        }
        return List.of();
    }
}
