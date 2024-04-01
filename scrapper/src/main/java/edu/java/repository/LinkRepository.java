package edu.java.repository;

import edu.java.entity.LinkEntity;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    LinkEntity add(String name);

    List<LinkEntity> findLinksByChatId(long id);

    LinkEntity update(LinkEntity link);

    Optional<LinkEntity> getByLinkName(String name);

    List<LinkEntity> findAllLastUpdated(Duration offset);

    void remove(int id);
}
