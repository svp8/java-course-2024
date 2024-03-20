package edu.java.service;

import edu.java.dto.Link;
import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.InvalidLinkFormatException;
import edu.java.exception.LinkNotTrackedException;
import edu.java.exception.NoSuchLinkException;
import edu.java.exception.URIException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

public class JpaLinkService implements LinkService {
    public static final String CHAT_ISN_T_REGISTERED = "Chat isn`t registered";
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    public JpaLinkService(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    private void checkChatIdInDb(long chatId) {
        //if no such chat in db, throw exception
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new InvalidChatIdException(HttpStatus.NOT_FOUND.value(), CHAT_ISN_T_REGISTERED);
        }
    }

    @Override
    public Link track(String name, long chatId) {
        checkChatIdInDb(chatId);
        if (name.startsWith("https://github.com")) {
            LinkUtils.parseGithubLink(name);
        } else if (name.startsWith("https://stackoverflow.com/questions/")) {
            LinkUtils.parseStackLink(name);
        } else {
            throw new InvalidLinkFormatException(HttpStatus.BAD_REQUEST.value(), "Wrong link format");
        }

        Optional<LinkEntity> link = linkRepository.findByName(name);
        LinkEntity linkEntity;
        if (link.isEmpty()) {
            linkEntity = linkRepository.save(LinkEntity.builder()
                .name(name)
                .lastUpdatedAt(OffsetDateTime.now())
                .createdAt(OffsetDateTime.now())
                .build());
        } else {
            linkEntity = link.get();
        }
        List<LinkEntity> linkList = linkRepository.findByChatId(chatId);
        if (linkList.stream().anyMatch(l -> l.getName().equals(name))) {
            throw new DuplicateLinkException(
                HttpStatus.BAD_REQUEST.value(),
                "Link is already tracked by this chat"
            );
        }
        try {
            Link createdLink = new Link(new URI(name.trim()));
            chatLinkRepository.create(chatId, linkEntity.getId());
            return createdLink;
        } catch (URISyntaxException e) {
            throw new URIException(HttpStatus.BAD_REQUEST.value(), "Bad Uri");
        }
    }

    @Override
    public void untrack(String name, long chatId) {
        checkChatIdInDb(chatId);
        Optional<LinkEntity> link = linkRepository.findByName(name);
        if (link.isEmpty()) {
            throw new NoSuchLinkException(HttpStatus.NOT_FOUND.value(), "Link is not created");
        } else {
            List<LinkEntity> linkList = linkRepository.findByChatId(chatId);
            //если к чату не привязана ссылка
            if (linkList == null || linkList.stream().noneMatch(l -> l.getName().equals(name))) {
                throw new LinkNotTrackedException(HttpStatus.NOT_FOUND.value(), "Link is not tracked by this chat");
            }
            chatLinkRepository.remove(chatId, link.get().getId());
            List<ChatEntity> chats = chatLinkRepository.findChatsByLinkId(link.get().getId());
            if (chats == null || chats.isEmpty()) {
                //delete link and all connected
                linkRepository.deleteById(link.get().getId());
            }
        }
    }

    @Override
    public List<Link> getAllByChatId(long chatId) {
        checkChatIdInDb(chatId);
        List<LinkEntity> allByChatId = linkRepository.findByChatId(chatId);
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

    @Override
    public Link update(LinkEntity linkEntity) {
        Link link;
        try {
            link = new Link(new URI(linkEntity.getName()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        //change last_updated_at
        linkRepository.save(new LinkEntity(linkEntity.getId(), linkEntity.getName(), linkEntity.getCreatedAt(),
            OffsetDateTime.now()
        ));
        return link;
    }
}
