package edu.java.repository;

import edu.java.dto.Link;
import edu.java.entity.LinkEntity;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    LinkEntity add(String name,long chatId);
    Optional<LinkEntity> getByLinkName(String name);
    List<LinkEntity> findAllByChatId(long chatId);
    List<LinkEntity> findAllLastUpdated();
    LinkEntity remove(int id);
}
