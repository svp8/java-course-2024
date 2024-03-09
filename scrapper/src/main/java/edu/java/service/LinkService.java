package edu.java.service;

import edu.java.dto.Link;
import java.util.List;
import java.util.Optional;

public interface LinkService {
    Link track(String name, long chatId);
    void untrack(String name, long chatId);
    List<Link> getAllByChatId(long chatId);
}
