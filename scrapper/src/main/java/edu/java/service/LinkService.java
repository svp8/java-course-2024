package edu.java.service;

import edu.java.dto.Link;
import edu.java.entity.LinkEntity;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface LinkService {
    @Transactional
    Link track(String name, long chatId);
    @Transactional

    void untrack(String name, long chatId);

    List<Link> getAllByChatId(long chatId);
    @Transactional

    Link update(LinkEntity linkEntity);
}
