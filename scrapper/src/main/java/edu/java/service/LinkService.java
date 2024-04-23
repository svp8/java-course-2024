package edu.java.service;

import edu.java.dto.Link;
import edu.java.entity.LinkEntity;
import java.util.List;

public interface LinkService {
    Link track(String name, long chatId);

    void untrack(String name, long chatId);

    List<Link> getAllByChatId(long chatId);

    Link update(LinkEntity linkEntity);
}
