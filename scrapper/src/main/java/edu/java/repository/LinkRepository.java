package edu.java.repository;

import edu.java.entity.LinkEntity;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    LinkEntity add(String name);

    LinkEntity update(LinkEntity link);

    Optional<LinkEntity> getByLinkName(String name);

    List<LinkEntity> findAllByChatId(long chatId);

    List<LinkEntity> findAllLastUpdated();

    void remove(int id);
}
