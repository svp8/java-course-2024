package edu.java.service.jpa;

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
import edu.java.service.LinkService;
import edu.java.utils.LinkUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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

    private ChatEntity checkChatIdInDb(long chatId) {
        //if no such chat in db, throw exception
        Optional<ChatEntity> chatEntity = chatRepository.findById(chatId);
        if (chatEntity.isEmpty()) {
            throw new InvalidChatIdException(HttpStatus.NOT_FOUND.value(), CHAT_ISN_T_REGISTERED);
        }
        return chatEntity.get();
    }

    @Override
    public Link track(String name, long chatId) {
        ChatEntity chatEntity = checkChatIdInDb(chatId);
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
            linkEntity = LinkEntity.builder()
                .name(name)
                .lastUpdatedAt(OffsetDateTime.now())
                .createdAt(OffsetDateTime.now())
                .chats(new ArrayList<>())
                .build();
            linkEntity.getChats().add(chatEntity);
        } else {
            linkEntity = link.get();
            List<LinkEntity> linkList = linkRepository.findByChats_Id(chatId);
            if (linkList.stream().anyMatch(l -> l.getName().equals(name))) {
                throw new DuplicateLinkException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Link is already tracked by this chat"
                );
            }
            linkEntity.getChats().add(chatEntity);
        }
        linkEntity = linkRepository.save(linkEntity);
        try {
            Link createdLink = new Link(new URI(linkEntity.getName().trim()));
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
            LinkEntity linkEntity = link.get();
            List<LinkEntity> linkList = linkRepository.findByChats_Id(chatId);
            //если к чату не привязана ссылка
            if (linkList == null || linkList.stream().noneMatch(l -> l.getName().equals(name))) {
                throw new LinkNotTrackedException(HttpStatus.NOT_FOUND.value(), "Link is not tracked by this chat");
            }
            List<ChatEntity> chats =
                new ArrayList<>(linkEntity.getChats().stream().filter(x -> x.getId() != chatId).toList());
            linkEntity.setChats(chats);
            linkRepository.save(linkEntity);
            if (linkEntity.getChats().isEmpty()) {
                linkRepository.deleteById(linkEntity.getId());
            }
        }
    }

    @Override
    public List<Link> getAllByChatId(long chatId) {
        checkChatIdInDb(chatId);
        List<LinkEntity> allByChatId = linkRepository.findByChats_Id(chatId);
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
        linkEntity.setLastUpdatedAt(OffsetDateTime.now());
        linkRepository.save(linkEntity);
        return link;
    }

    @Override
    public List<LinkEntity> findAllLastUpdated(Duration interval) {

        return linkRepository.findAllLastUpdated(interval.toSeconds());
    }
}
