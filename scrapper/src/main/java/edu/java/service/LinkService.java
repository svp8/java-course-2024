package edu.java.service;

import edu.java.dto.Link;
import java.util.List;
import java.util.Optional;

public interface LinkService {
    Optional<Link> getByNameAndChatId(String name, long chatId);

    Link track(String name, long chatId);

    void registerChatId(long chatId);

    void untrack(String name, long chatId);

    List<Link> getAllByChatId(long chatId);
}
