package edu.java.service;

import edu.java.dto.Link;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.NoSuchLinkException;
import edu.java.exception.URIException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LinkServiceImpl implements LinkService {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String CHAT_ISN_T_REGISTERED = "Chat isn`t registered";
    private Map<Long, List<Link>> inMemoryDb;

    public Map<Long, List<Link>> getInMemoryDb() {
        return inMemoryDb;
    }

    public LinkServiceImpl() {
        this.inMemoryDb = new ConcurrentHashMap<>();
    }

    public Optional<Link> getByNameAndChatId(String name, long chatId) {
        if (inMemoryDb.get(chatId) == null) {
            return Optional.empty();
        }
        return inMemoryDb.get(chatId).stream().filter(x -> x.getUri().toString().equals(name)).findFirst();
    }

    public Link track(String name, long chatId) {
        checkChatIdInDb(chatId);
        Optional<Link> link = getByNameAndChatId(name, chatId);
        if (link.isPresent()) {
            throw new DuplicateLinkException(HttpStatus.BAD_REQUEST.value(), "Link is already tracked");
        } else {
            try {
                Link createdLink = new Link(new URI(name.trim()));
                inMemoryDb.get(chatId).add(createdLink);
                return createdLink;
            } catch (URISyntaxException e) {
                throw new URIException(HttpStatus.BAD_REQUEST.value(), "Bad Uri");
            }
        }
    }

    public void registerChatId(long chatId) {
        if (inMemoryDb.containsKey(chatId)) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat already registered");
        } else {
            inMemoryDb.put(chatId, new ArrayList<>());
        }
    }

    public void untrack(String name, long chatId) {
        checkChatIdInDb(chatId);
        Optional<Link> link = getByNameAndChatId(name.trim(), chatId);
        if (link.isPresent()) {
            //delete in db
            List<Link> links = inMemoryDb.get(chatId);
            links.remove(link.get());
        } else {
            throw new NoSuchLinkException(HttpStatus.NOT_FOUND.value(), "No such link");
        }
    }

    private void checkChatIdInDb(long chatId) {
        //if no such chat in db, throw exception
        if (!inMemoryDb.containsKey(chatId)) {
            throw new InvalidChatIdException(HttpStatus.NOT_FOUND.value(), CHAT_ISN_T_REGISTERED);
        }
    }

    public List<Link> getAllByChatId(long chatId) {
        checkChatIdInDb(chatId);
        return inMemoryDb.get(chatId);
    }
}
